package tornado.drivers.opencl.graal;

import java.nio.ByteOrder;
import jdk.vm.ci.code.Architecture;
import jdk.vm.ci.code.Register;
import jdk.vm.ci.code.Register.RegisterCategory;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.PlatformKind;
import tornado.drivers.opencl.graal.lir.OCLKind;
import tornado.drivers.opencl.graal.meta.OCLMemorySpace;

import static jdk.vm.ci.code.MemoryBarriers.LOAD_STORE;
import static jdk.vm.ci.code.MemoryBarriers.STORE_STORE;
import static tornado.common.exceptions.TornadoInternalError.shouldNotReachHere;
import static tornado.drivers.opencl.graal.asm.OCLAssemblerConstants.*;

public class OCLArchitecture extends Architecture {

    public static final RegisterCategory OCL_ABI = new RegisterCategory("abi");

    public static class OCLRegister {

        public final int number;
        public final String name;
        public final OCLKind lirKind;

        public OCLRegister(int number, String name, OCLKind lirKind) {
            this.number = number;
            this.name = name;
            this.lirKind = lirKind;

        }

        public Register asRegister() {
            return new Register(number, 0, name, OCL_ABI);
        }

        public String getDeclaration() {
            return String.format("%s %s", lirKind.toString(), name);
        }

    }

    public static class OCLMemoryBase extends OCLRegister {

        public final OCLMemorySpace memorySpace;

        public OCLMemoryBase(int number, String name, OCLMemorySpace memorySpace) {
            super(number, name, OCLKind.UCHAR);
            this.memorySpace = memorySpace;
        }

        @Override
        public String getDeclaration() {
            return String.format("%s %s *%s", memorySpace.name(), lirKind.toString(), name);
        }

    }

    public static final OCLMemoryBase hp = new OCLMemoryBase(0, HEAP_REF_NAME, OCLMemorySpace.GLOBAL);
    public static OCLRegister sp;
    public static final OCLMemoryBase cp = new OCLMemoryBase(2, CONSTANT_REGION_NAME, OCLMemorySpace.CONSTANT);
    public static final OCLMemoryBase lp = new OCLMemoryBase(3, LOCAL_REGION_NAME, OCLMemorySpace.LOCAL);
    public static final OCLMemoryBase pp = new OCLMemoryBase(4, PRIVATE_REGION_NAME, OCLMemorySpace.GLOBAL);

    public static OCLRegister[] abiRegisters;

    public OCLArchitecture(final OCLKind wordKind, final ByteOrder byteOrder) {
        super("Tornado OpenCL", wordKind, byteOrder, false, null, LOAD_STORE | STORE_STORE, 0, 0);
        sp = new OCLRegister(1, STACK_REF_NAME, wordKind);
        abiRegisters = new OCLRegister[]{hp, sp, cp, lp, pp};
    }

    @Override
    public PlatformKind getPlatformKind(JavaKind javaKind) {
        OCLKind oclKind = OCLKind.ILLEGAL;
        switch (javaKind) {
            case Boolean:
                oclKind = OCLKind.BOOL;
                break;
            case Byte:
                oclKind = OCLKind.CHAR;
                break;
            case Short:
                oclKind = (javaKind.isUnsigned()) ? OCLKind.USHORT : OCLKind.SHORT;
                break;
            case Char:
                oclKind = OCLKind.USHORT;
                break;
            case Int:
                oclKind = (javaKind.isUnsigned()) ? OCLKind.UINT : OCLKind.INT;
                break;
            case Long:
                oclKind = (javaKind.isUnsigned()) ? OCLKind.ULONG : OCLKind.LONG;
                break;
            case Float:
                oclKind = OCLKind.FLOAT;
                break;
            case Double:
                oclKind = OCLKind.DOUBLE;
                break;
            case Object:
                oclKind = (OCLKind) getWordKind();
                break;
            case Illegal:
                oclKind = OCLKind.ILLEGAL;
                break;
            default:
                shouldNotReachHere("illegal java type for %s", javaKind.name());
        }

        return oclKind;
    }

    @Override
    public int getReturnAddressSize() {
        return this.getWordSize();
    }

    @Override
    public boolean canStoreValue(RegisterCategory category, PlatformKind platformKind) {

        return false;
    }

    @Override
    public PlatformKind getLargestStorableKind(RegisterCategory category) {
        return OCLKind.LONG;
    }

    public String getABI() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < abiRegisters.length; i++) {
            sb.append(abiRegisters[i].getDeclaration());
            if (i < abiRegisters.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
