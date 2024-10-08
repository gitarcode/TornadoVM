/*
 * Copyright (c) 2024, APT Group, Department of Computer Science,
 * The University of Manchester.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.manchester.tornado.api.types.tensors;

import static java.lang.foreign.ValueLayout.JAVA_DOUBLE;

import java.lang.foreign.MemorySegment;
import java.nio.DoubleBuffer;
import java.util.Arrays;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.internal.annotations.SegmentElementSize;
import uk.ac.manchester.tornado.api.types.arrays.DoubleArray;
import uk.ac.manchester.tornado.api.types.arrays.TornadoNativeArray;

@SegmentElementSize(size = 8)
public final class TensorFP64 extends Tensor {

  private static final int DOUBLE_BYTES = 8;

  /** The data type of the elements contained within the tensor. */
  private final DType dType;

  private final Shape shape;

  private final DoubleArray tensorStorage;

  /** The total number of elements in the tensor. */
  private int numberOfElements;

  /** The memory segment representing the tensor data in native memory. */
  public TensorFP64(Shape shape) {
    super(DType.DOUBLE, shape);
    this.shape = shape;
    this.numberOfElements = shape.getSize();
    this.dType = DType.DOUBLE;
    this.tensorStorage = new DoubleArray(numberOfElements);
  }

  public void init(double value) {
    for (int i = 0; i < getSize(); i++) {
      tensorStorage.getSegmentWithHeader().setAtIndex(JAVA_DOUBLE, getBaseIndex() + i, value);
    }
  }

  public void set(int index, double value) {
    tensorStorage.getSegmentWithHeader().setAtIndex(JAVA_DOUBLE, getBaseIndex() + index, value);
  }

  private long getBaseIndex() {
    return (int) TornadoNativeArray.ARRAY_HEADER / DOUBLE_BYTES;
  }

  /**
   * Gets the double value stored at the specified index of the {@link DoubleArray} instance.
   *
   * @param index The index of which to retrieve the double value.
   * @return
   */
  public double get(int index) {
    return tensorStorage.getSegmentWithHeader().getAtIndex(JAVA_DOUBLE, getBaseIndex() + index);
  }

  @Override
  public int getSize() {
    return numberOfElements;
  }

  @Override
  public MemorySegment getSegment() {
    return tensorStorage.getSegment();
  }

  @Override
  public MemorySegment getSegmentWithHeader() {
    return tensorStorage.getSegmentWithHeader();
  }

  @Override
  public long getNumBytesOfSegmentWithHeader() {
    return tensorStorage.getNumBytesOfSegmentWithHeader();
  }

  @Override
  public long getNumBytesOfSegment() {
    return tensorStorage.getNumBytesOfSegment();
  }

  @Override
  protected void clear() {
    init(0d);
  }

  @Override
  public int getElementSize() {
    return DOUBLE_BYTES;
  }

  @Override
  public Shape getShape() {
    return this.shape;
  }

  @Override
  public String getDTypeAsString() {
    return dType.toString();
  }

  @Override
  public DType getDType() {
    return dType;
  }

  public DoubleBuffer getDoubleBuffer() {
    return getSegment().asByteBuffer().asDoubleBuffer();
  }

  public static void initialize(TensorFP64 tensor, short value) {
    for (@Parallel int i = 0; i < tensor.getSize(); i++) {
      tensor.set(i, value);
    }
  }

  /**
   * Concatenates multiple {@link TensorFP64} instances into a single {@link TensorFP64}.
   *
   * @param arrays Variable number of {@link TensorFP64} objects to be concatenated.
   * @return A new {@link TensorFP64} instance containing all the elements of the input arrays,
   *     concatenated in the order they were provided.
   */
  public static TensorFP64 concat(TensorFP64... arrays) {
    int newSize = Arrays.stream(arrays).mapToInt(TensorFP64::getSize).sum();
    TensorFP64 concatArray = new TensorFP64(new Shape(newSize));
    long currentPositionBytes = 0;
    for (TensorFP64 array : arrays) {
      MemorySegment.copy(
          array.getSegment(),
          0,
          concatArray.getSegment(),
          currentPositionBytes,
          array.getNumBytesOfSegment());
      currentPositionBytes += array.getNumBytesOfSegment();
    }
    return concatArray;
  }
}
