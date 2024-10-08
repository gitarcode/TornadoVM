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

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

import java.lang.foreign.MemorySegment;
import java.nio.FloatBuffer;
import java.util.Arrays;
import uk.ac.manchester.tornado.api.annotations.Parallel;
import uk.ac.manchester.tornado.api.internal.annotations.SegmentElementSize;
import uk.ac.manchester.tornado.api.types.arrays.FloatArray;
import uk.ac.manchester.tornado.api.types.arrays.TornadoNativeArray;

@SegmentElementSize(size = 4)
public final class TensorFP32 extends Tensor {

  private static final int FLOAT_BYTES = 4;

  /** The data type of the elements contained within the tensor. */
  private final DType dType;

  private final Shape shape;

  private final FloatArray tensorStorage;

  /** The total number of elements in the tensor. */
  private int numberOfElements;

  /** The memory segment representing the tensor data in native memory. */
  public TensorFP32(Shape shape) {
    super(DType.FLOAT, shape);
    this.shape = shape;
    this.numberOfElements = shape.getSize();
    this.dType = DType.FLOAT;
    this.tensorStorage = new FloatArray(numberOfElements);
  }

  public void init(float value) {
    for (int i = 0; i < getSize(); i++) {
      tensorStorage.getSegmentWithHeader().setAtIndex(JAVA_FLOAT, getBaseIndex() + i, value);
    }
  }

  public void set(int index, float value) {
    tensorStorage.getSegmentWithHeader().setAtIndex(JAVA_FLOAT, getBaseIndex() + index, value);
  }

  private long getBaseIndex() {
    return (int) TornadoNativeArray.ARRAY_HEADER / FLOAT_BYTES;
  }

  /**
   * Gets the float value stored at the specified index of the {@link FloatArray} instance.
   *
   * @param index The index of which to retrieve the float value.
   * @return
   */
  public float get(int index) {
    return tensorStorage.getSegmentWithHeader().getAtIndex(JAVA_FLOAT, getBaseIndex() + index);
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
    init(0.0f);
  }

  @Override
  public int getElementSize() {
    return DType.FLOAT.getByteSize();
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

  public float[] toHeapArray() {
    float[] outputArray = new float[getSize()];
    for (int i = 0; i < getSize(); i++) {
      outputArray[i] = get(i);
    }
    return outputArray;
  }

  public FloatBuffer getFloatBuffer() {
    return getSegment().asByteBuffer().asFloatBuffer();
  }

  public static void initialize(TensorFP32 tensor, short value) {
    for (@Parallel int i = 0; i < tensor.getSize(); i++) {
      tensor.set(i, value);
    }
  }

  /**
   * Concatenates multiple {@link TensorFP32} instances into a single {@link TensorFP32}.
   *
   * @param arrays Variable number of {@link TensorFP32} objects to be concatenated.
   * @return A new {@link TensorFP32} instance containing all the elements of the input arrays,
   *     concatenated in the order they were provided.
   */
  public static TensorFP32 concat(TensorFP32... arrays) {
    int newSize = Arrays.stream(arrays).mapToInt(TensorFP32::getSize).sum();
    TensorFP32 concatArray = new TensorFP32(new Shape(newSize));
    long currentPositionBytes = 0;
    for (TensorFP32 array : arrays) {
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
