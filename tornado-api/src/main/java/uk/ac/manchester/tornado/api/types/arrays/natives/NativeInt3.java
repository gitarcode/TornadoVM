/*
 * This file is part of Tornado: A heterogeneous programming framework:
 * https://github.com/beehive-lab/tornadovm
 *
 * Copyright (c) 2023, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * GNU Classpath is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * GNU Classpath is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GNU Classpath; see the file COPYING. If not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library. Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module. An independent module is a module which is not derived from
 * or based on this library. If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obligated to do so. If you do not wish to do so, delete this
 * exception statement from your version.
 *
 */
package uk.ac.manchester.tornado.api.types.arrays.natives;

import java.nio.IntBuffer;

import uk.ac.manchester.tornado.api.internal.annotations.Payload;
import uk.ac.manchester.tornado.api.internal.annotations.Vector;
import uk.ac.manchester.tornado.api.math.TornadoMath;
import uk.ac.manchester.tornado.api.types.arrays.IntArray;
import uk.ac.manchester.tornado.api.types.vectors.Int2;

@Vector
public final class NativeInt3 implements TornadoNativeCollectionsInterface<IntBuffer> {
    public static final Class<NativeInt3> TYPE = NativeInt3.class;

    private static final String NUMBER_FORMAT = "{ x=%-7d, y=%-7d, z=%-7d }";

    /**
     * number of elements in the storage.
     */
    private static final int NUM_ELEMENTS = 3;

    /**
     * backing array.
     */
    @Payload
    private final NativeVectorInt nativeVectorInt;

    public NativeInt3(NativeVectorInt storage) {
        this.nativeVectorInt = storage;
    }

    public NativeInt3() {
        this(new NativeVectorInt(NUM_ELEMENTS));
    }

    public NativeInt3(int x, int y, int z) {
        this();
        setX(x);
        setY(y);
        setZ(z);
    }

    /*
     * vector = op( vector, vector )
     */
    public static NativeInt3 add(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }

    public static NativeInt3 sub(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static NativeInt3 div(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(a.getX() / b.getX(), a.getY() / b.getY(), a.getZ() / b.getZ());
    }

    public static NativeInt3 mult(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(a.getX() * b.getX(), a.getY() * b.getY(), a.getZ() * b.getZ());
    }

    public static NativeInt3 min(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(Math.min(a.getX(), b.getX()), Math.min(a.getY(), b.getY()), Math.min(a.getZ(), b.getZ()));
    }

    public static NativeInt3 max(NativeInt3 a, NativeInt3 b) {
        return new NativeInt3(Math.max(a.getX(), b.getX()), Math.max(a.getY(), b.getY()), Math.max(a.getZ(), b.getZ()));
    }

    /*
     * vector = op (vector, scalar)
     */
    public static NativeInt3 add(NativeInt3 a, int b) {
        return new NativeInt3(a.getX() + b, a.getY() + b, a.getZ() + b);
    }

    public static NativeInt3 sub(NativeInt3 a, int b) {
        return new NativeInt3(a.getX() - b, a.getY() - b, a.getZ() - b);
    }

    public static NativeInt3 mult(NativeInt3 a, int b) {
        return new NativeInt3(a.getX() * b, a.getY() * b, a.getZ() * b);
    }

    public static NativeInt3 div(NativeInt3 a, int b) {
        return new NativeInt3(a.getX() / b, a.getY() / b, a.getZ() / b);
    }

    public static NativeInt3 inc(NativeInt3 a, int value) {
        return add(a, value);
    }

    public static NativeInt3 dec(NativeInt3 a, int value) {
        return sub(a, value);
    }

    public static NativeInt3 scale(NativeInt3 a, int value) {
        return mult(a, value);
    }

    public static NativeInt3 clamp(NativeInt3 x, int min, int max) {
        return new NativeInt3(TornadoMath.clamp(x.getX(), min, max), TornadoMath.clamp(x.getY(), min, max), TornadoMath.clamp(x.getZ(), min, max));
    }

    /*
     * vector wide operations
     */
    public static int min(NativeInt3 value) {
        return Math.min(value.getX(), Math.min(value.getY(), value.getZ()));
    }

    public static int max(NativeInt3 value) {
        return Math.max(value.getX(), Math.max(value.getY(), value.getZ()));
    }

    public static boolean isEqual(NativeInt3 a, NativeInt3 b) {
        return TornadoMath.isEqual(a.asBuffer().array(), b.asBuffer().array());
    }

    static NativeInt3 loadFromArray(final IntArray array, int index) {
        final NativeInt3 result = new NativeInt3();
        result.setX(array.get(index));
        result.setY(array.get(index + 1));
        result.setZ(array.get(index + 2));
        return result;
    }

    public int get(int index) {
        return nativeVectorInt.get(index);
    }

    public void set(int index, int value) {
        nativeVectorInt.set(index, value);
    }

    public void set(NativeInt3 value) {
        setX(value.getX());
        setY(value.getY());
        setZ(value.getZ());
    }

    public int getX() {
        return get(0);
    }

    public void setX(int value) {
        set(0, value);
    }

    public int getY() {
        return get(1);
    }

    public void setY(int value) {
        set(1, value);
    }

    public int getZ() {
        return get(2);
    }

    public void setZ(int value) {
        set(2, value);
    }

    /**
     * Duplicates this vector.
     *
     * @return {@link NativeInt3}
     */
    public NativeInt3 duplicate() {
        NativeInt3 vector = new NativeInt3();
        vector.set(this);
        return vector;
    }

    public Int2 asInt2() {
        return new Int2(getX(), getY());
    }

    public String toString(String fmt) {
        return String.format(fmt, getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return toString(NUMBER_FORMAT);
    }

    @Override
    public void loadFromBuffer(IntBuffer buffer) {
        asBuffer().put(buffer);
    }

    @Override
    public IntBuffer asBuffer() {
        return nativeVectorInt.getSegment().asByteBuffer().asIntBuffer();
    }

    @Override
    public int size() {
        return NUM_ELEMENTS;
    }

    void storeToArray(final IntArray array, int index) {
        array.set(index, getX());
        array.set(index + 1, getY());
        array.set(index + 2, getZ());
    }

    public void clear() {
        nativeVectorInt.clear();
    }
}