package tornado.collections.types;

import java.nio.IntBuffer;
import tornado.api.Payload;
import tornado.api.Vector;
import tornado.collections.math.TornadoMath;

/**
 * Class that represents a vector of 4x ints e.g. <int,int,int,int>
 *
 * @author jamesclarkson
 *
 */
@Vector
public class Int4 implements PrimitiveStorage<IntBuffer> {

    public static final Class<Int4> TYPE = Int4.class;

    private static final String numberFormat = "{ x=%-7d, y=%-7d, z=%-7d, w=%-7d }";

    /**
     * backing array
     */
    @Payload
    final protected int[] storage;

    /**
     * number of elements in the storage
     */
    final private static int numElements = 4;

    public Int4(int[] storage) {
        this.storage = storage;
    }

    public Int4() {
        this(new int[numElements]);
    }

    public Int4(int x, int y, int z, int w) {
        this();
        setX(x);
        setY(y);
        setZ(z);
        setW(w);
    }

    public int get(int index) {
        return storage[index];
    }

    public void set(int index, int value) {
        storage[index] = value;
    }

    public void set(Int4 value) {
        setX(value.getX());
        setY(value.getY());
        setZ(value.getZ());
        setW(value.getW());
    }

    public int getX() {
        return get(0);
    }

    public int getY() {
        return get(1);
    }

    public int getZ() {
        return get(2);
    }

    public int getW() {
        return get(3);
    }

    public void setX(int value) {
        set(0, value);
    }

    public void setY(int value) {
        set(1, value);
    }

    public void setZ(int value) {
        set(2, value);
    }

    public void setW(int value) {
        set(3, value);
    }

    /**
     * Duplicates this vector
     *
     * @return
     */
    public Int4 duplicate() {
        Int4 vector = new Int4();
        vector.set(this);
        return vector;
    }

    public Int2 asInt2() {
        return new Int2(getX(), getY());
    }

    public Int3 asInt3() {
        return new Int3(getX(), getY(), getZ());
    }

    public String toString(String fmt) {
        return String.format(fmt, getX(), getY(), getZ(), getW());
    }

    public String toString() {
        return toString(numberFormat);
    }

    protected static final Int4 loadFromArray(final int[] array, int index) {
        final Int4 result = new Int4();
        result.setX(array[index]);
        result.setY(array[index + 1]);
        result.setZ(array[index + 2]);
        result.setW(array[index + 3]);
        return result;
    }

    protected final void storeToArray(final int[] array, int index) {
        array[index] = getX();
        array[index + 1] = getY();
        array[index + 2] = getZ();
        array[index + 3] = getW();
    }

    @Override
    public void loadFromBuffer(IntBuffer buffer) {
        asBuffer().put(buffer);
    }

    @Override
    public IntBuffer asBuffer() {
        return IntBuffer.wrap(storage);
    }

    public int size() {
        return numElements;
    }

    /*
	 * vector = op( vector, vector )
     */
    public static Int4 add(Int4 a, Int4 b) {
        return new Int4(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ(), a.getW() + b.getW());
    }

    public static Int4 sub(Int4 a, Int4 b) {
        return new Int4(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ(), a.getW() - b.getW());
    }

    public static Int4 div(Int4 a, Int4 b) {
        return new Int4(a.getX() / b.getX(), a.getY() / b.getY(), a.getZ() / b.getZ(), a.getW() / b.getW());
    }

    public static Int4 mult(Int4 a, Int4 b) {
        return new Int4(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ(), a.getW() * b.getW());
    }

    public static Int4 min(Int4 a, Int4 b) {
        return new Int4(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()), Math.min(a.getW(), b.getW()));
    }

    public static Int4 max(Int4 a, Int4 b) {
        return new Int4(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()), Math.max(a.getW(), b.getW()));
    }

    /*
	 * vector = op (vector, scalar)
     */
    public static Int4 add(Int4 a, int b) {
        return new Int4(a.getX() + b, a.getY() + b, a.getZ() + b, a.getW() + b);
    }

    public static Int4 sub(Int4 a, int b) {
        return new Int4(a.getX() - b, a.getY() - b, a.getZ() - b, a.getW() - b);
    }

    public static Int4 mult(Int4 a, int b) {
        return new Int4(a.getX() * b, a.getY() * b, a.getZ() * b, a.getW() * b);
    }

    public static Int4 div(Int4 a, int b) {
        return new Int4(a.getX() / b, a.getY() / b, a.getZ() / b, a.getW() / b);
    }

    /*
	 * vector = op (vector, vector)
     */
    public static void add(Int4 a, Int4 b, Int4 c) {
        c.setX(a.getX() + b.getX());
        c.setY(a.getY() + b.getY());
        c.setZ(a.getZ() + b.getZ());
        c.setW(a.getW() + b.getW());
    }

    public static void sub(Int4 a, Int4 b, Int4 c) {
        c.setX(a.getX() - b.getX());
        c.setY(a.getY() - b.getY());
        c.setZ(a.getZ() - b.getZ());
        c.setW(a.getW() - b.getW());
    }

    public static void mult(Int4 a, Int4 b, Int4 c) {
        c.setX(a.getX() * b.getX());
        c.setY(a.getY() * b.getY());
        c.setZ(a.getZ() * b.getZ());
        c.setW(a.getW() * b.getW());
    }

    public static void div(Int4 a, Int4 b, Int4 c) {
        c.setX(a.getX() / b.getX());
        c.setY(a.getY() / b.getY());
        c.setZ(a.getZ() / b.getZ());
        c.setW(a.getW() / b.getW());
    }

    public static void min(Int4 a, Int4 b, Int4 c) {
        c.setX(Math.min(a.getX(), b.getX()));
        c.setY(Math.min(a.getY(), b.getY()));
        c.setZ(Math.min(a.getZ(), b.getZ()));
        c.setW(Math.min(a.getW(), b.getW()));
    }

    public static void max(Int4 a, Int4 b, Int4 c) {
        c.setX(Math.max(a.getX(), b.getX()));
        c.setY(Math.max(a.getY(), b.getY()));
        c.setZ(Math.max(a.getZ(), b.getZ()));
        c.setW(Math.max(a.getW(), b.getW()));
    }

    /*
	 *  inplace src = op (src, scalar)
     */
    public static void inc(Int4 a, int value) {
        a.setX(a.getX() + value);
        a.setY(a.getY() + value);
        a.setZ(a.getZ() + value);
        a.setW(a.getW() + value);
    }

    public static void dec(Int4 a, int value) {
        a.setX(a.getX() - value);
        a.setY(a.getY() - value);
        a.setZ(a.getZ() - value);
        a.setW(a.getW() - value);
    }

    public static void scale(Int4 a, int value) {
        a.setX(a.getX() * value);
        a.setY(a.getY() * value);
        a.setZ(a.getZ() * value);
        a.setW(a.getW() * value);
    }

    /*
	 * misc inplace vector ops
     */
    public static void clamp(Int4 x, int min, int max) {
        x.setX(TornadoMath.clamp(x.getX(), min, max));
        x.setY(TornadoMath.clamp(x.getY(), min, max));
        x.setZ(TornadoMath.clamp(x.getZ(), min, max));
        x.setW(TornadoMath.clamp(x.getW(), min, max));
    }

    /*
	 * vector wide operations
     */
    public static int min(Int4 value) {
        return Math.min(value.getX(), Math.min(value.getY(), Math.min(value.getZ(),value.getW())));
    }

    public static int max(Int4 value) {
        return Math.max(value.getX(), Math.max(value.getY(), Math.max(value.getZ(),value.getW())));
    }

    public static boolean isEqual(Int4 a, Int4 b) {
        return TornadoMath.isEqual(a.asBuffer().array(), b.asBuffer().array());
    }
}