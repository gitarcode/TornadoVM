package uk.ac.manchester.tornado.drivers.ptx;

import uk.ac.manchester.tornado.api.TornadoDeviceContext;
import uk.ac.manchester.tornado.api.WorkerGrid;
import uk.ac.manchester.tornado.api.common.Event;
import uk.ac.manchester.tornado.api.profiler.ProfilerType;
import uk.ac.manchester.tornado.drivers.ptx.graal.compiler.PTXCompilationResult;
import uk.ac.manchester.tornado.drivers.ptx.mm.PTXCallStack;
import uk.ac.manchester.tornado.drivers.ptx.mm.PTXMemoryManager;
import uk.ac.manchester.tornado.drivers.ptx.runtime.PTXTornadoDevice;
import uk.ac.manchester.tornado.runtime.common.CallStack;
import uk.ac.manchester.tornado.runtime.common.Initialisable;
import uk.ac.manchester.tornado.runtime.common.TornadoInstalledCode;
import uk.ac.manchester.tornado.runtime.common.TornadoLogger;
import uk.ac.manchester.tornado.runtime.common.TornadoOptions;
import uk.ac.manchester.tornado.runtime.tasks.meta.TaskMetaData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;


public class PTXDeviceContext extends TornadoLogger implements Initialisable, TornadoDeviceContext {

    private final PTXDevice device;
    private final PTXMemoryManager memoryManager;
    private final PTXStream stream;
    private final PTXCodeCache codeCache;
    private final PTXScheduler scheduler;
    private boolean wasReset;

    public PTXDeviceContext(PTXDevice device, PTXStream stream) {
        this.device = device;
        this.stream = stream;

        this.scheduler = new PTXScheduler(device);
        codeCache = new PTXCodeCache(this);
        memoryManager = new PTXMemoryManager(this);
        wasReset = false;
    }

    @Override
    public PTXMemoryManager getMemoryManager() {
        return memoryManager;
    }

    @Override
    public boolean needsBump() {
        return false;
    }

    @Override
    public boolean wasReset() {
        return wasReset;
    }

    @Override
    public void setResetToFalse() {
        wasReset = false;
    }

    @Override
    public boolean isPlatformFPGA() {
        return false;
    }

    @Override
    public boolean useRelativeAddresses() {
        return false;
    }

    @Override
    public boolean isInitialised() {
        return memoryManager.isInitialised();
    }

    public PTXTornadoDevice asMapping() {
        return new PTXTornadoDevice(device.getDeviceIndex());
    }

    public TornadoInstalledCode installCode(PTXCompilationResult result, String resolvedMethodName) {
        return codeCache.installSource(result.getName(), result.getTargetCode(), result.getTaskMeta(), resolvedMethodName);
    }

    public TornadoInstalledCode installCode(String name, byte[] code, TaskMetaData taskMeta, String resolvedMethodName) {
        return codeCache.installSource(name, code, taskMeta, resolvedMethodName);
    }

    public TornadoInstalledCode getInstalledCode(String name) {
        return codeCache.getCachedCode(name);
    }

    public PTXCodeCache getCodeCache() {
        return codeCache;
    }

    public PTXDevice getDevice() {
        return device;
    }

    public ByteOrder getByteOrder() {
        return device.getByteOrder();
    }

    public Event resolveEvent(int event) {
        return stream.resolveEvent(event);
    }

    public void flushEvents() {
        sync();
    }

    public int enqueueBarrier() {
        return stream.enqueueBarrier();
    }

    public int enqueueBarrier(int[] events) {
        return stream.enqueueBarrier(events);
    }

    public int enqueueMarker() {
        // Since streams are always in-order in CUDA there is no difference
        // between marker and barrier
        return stream.enqueueBarrier();
    }

    public int enqueueMarker(int[] events) {
        // Since streams are always in-order in CUDA there is no difference
        // between marker and barrier
        return stream.enqueueBarrier(events);
    }

    public void sync() {
        stream.sync();
    }

    public void flush() {
        // I don't think there is anything like this in CUDA so I am calling sync
        sync();
    }

    public void reset() {
        stream.reset();
        memoryManager.reset();
        codeCache.reset();
        wasReset = true;
    }

    public int enqueueKernelLaunch(PTXModule module, CallStack stack, long batchThreads) {
        int[] blockDimension = { 1, 1, 1 };
        int[] gridDimension = { 1, 1, 1 };
        if (module.metaData.isWorkerGridAvailable()) {
            WorkerGrid grid = module.metaData.getWorkerGrid(module.metaData.getId());
            int[] global = Arrays.stream(grid.getGlobalWork()).mapToInt(l -> (int) l).toArray();

            if (grid.getLocalWork() != null) {
                blockDimension = Arrays.stream(grid.getLocalWork()).mapToInt(l -> (int) l).toArray();
            }
            else {
                blockDimension = scheduler.calculateBlockDimension(grid.getGlobalWork(), module.getMaxThreadBlocks(), grid.dimension(), module.javaName);
            }
            gridDimension = scheduler.calculateGridDimension(module.javaName, grid.dimension(), global, blockDimension);
        } else if (module.metaData.isParallel()) {
            scheduler.calculateGlobalWork(module.metaData, batchThreads);
            blockDimension = scheduler.calculateBlockDimension(module);
            gridDimension = scheduler.calculateGridDimension(module, blockDimension);
        }
        int kernelLaunchEvent = stream.enqueueKernelLaunch(module, writePTXStackOnDevice((PTXCallStack) stack), gridDimension, blockDimension);
        updateProfiler(kernelLaunchEvent, module.metaData);
        return kernelLaunchEvent;
    }

    private byte[] writePTXStackOnDevice(PTXCallStack stack) {
        ByteBuffer args = ByteBuffer.allocate(8);
        args.order(getByteOrder());

        // Stack pointer
        if (!stack.isOnDevice())
            stack.write();
        long address = stack.getAddress();
        args.putLong(address);

        return args.array();
    }

    private void updateProfiler(final int taskEvent, final TaskMetaData meta) {
        if (TornadoOptions.isProfilerEnabled()) {
            Event tornadoKernelEvent = resolveEvent(taskEvent);
            tornadoKernelEvent.waitForEvents();
            long timer = meta.getProfiler().getTimer(ProfilerType.TOTAL_KERNEL_TIME);
            // Register globalTime
            meta.getProfiler().setTimer(ProfilerType.TOTAL_KERNEL_TIME, timer + tornadoKernelEvent.getExecutionTime());
            // Register the time for the task
            meta.getProfiler().setTaskTimer(ProfilerType.TASK_KERNEL_TIME, meta.getId(), tornadoKernelEvent.getExecutionTime());
        }
    }

    public boolean shouldCompile(String name) {
        return !codeCache.isCached(name);
    }

    public void cleanup() {
        stream.cleanup();
    }

    /*
     * SYNC READS
     */
    public int readBuffer(long address, long length, byte[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, short[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, char[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, int[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, long[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, float[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    public int readBuffer(long address, long length, double[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueRead(address, length, array, hostOffset, waitEvents);
    }

    /*
     * ASYNC READS
     */
    public int enqueueReadBuffer(long address, long length, byte[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, short[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, char[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, int[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, long[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, float[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueReadBuffer(long address, long length, double[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncRead(address, length, array, hostOffset, waitEvents);
    }

    /*
     * SYNC WRITES
     */
    public void writeBuffer(long address, long length, byte[] array, long hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, short[] array, long hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, char[] array, long hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, int[] array, long hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, long[] array, int hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, float[] array, int hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    public void writeBuffer(long address, long length, double[] array, int hostOffset, int[] waitEvents) {
        stream.enqueueWrite(address, length, array, hostOffset, waitEvents);
    }

    /*
     * ASYNC WRITES
     */
    public int enqueueWriteBuffer(long address, long length, byte[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, short[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, char[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, int[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, long[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, float[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public int enqueueWriteBuffer(long address, long length, double[] array, long hostOffset, int[] waitEvents) {
        return stream.enqueueAsyncWrite(address, length, array, hostOffset, waitEvents);
    }

    public void dumpEvents() {
        List<PTXEvent> events = stream.getEventsWrapper().getEvents();

        final String deviceName = "PTX-" + device.getDeviceName();
        System.out.printf("Found %d events on device %s:\n", events.size(), deviceName);
        if (events.isEmpty()) {
            return;
        }

        System.out.println("event: device, type, info, status");
        events.forEach((e) -> {
            System.out.printf("event: %s, %s, %s\n", deviceName, e.getName(), e.getStatus());
        });
    }

}
