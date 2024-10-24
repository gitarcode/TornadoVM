/*
 * Copyright (c) 2021, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 * Copyright (c) 2009, 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package uk.ac.manchester.tornado.runtime.graal.nodes;

import org.graalvm.compiler.core.common.type.StampFactory;
import org.graalvm.compiler.graph.NodeClass;
import org.graalvm.compiler.nodeinfo.NodeInfo;
import org.graalvm.compiler.nodes.FixedWithNextNode;
import org.graalvm.compiler.nodes.ValueNode;
import org.graalvm.compiler.nodes.spi.Lowerable;
import org.graalvm.compiler.nodes.spi.LoweringTool;
import uk.ac.manchester.tornado.api.KernelContext;

/**
 * The {@link ThreadIdFixedWithNextNode} is used to replace the FieldNodes that correspond to the
 * {@link KernelContext}. In essence, these fields are: threadIdx, threadIdy and threadIdz.
 *
 * <p>During lowering, this node is replaced with a FloatingNode that corresponds to a TornadoVM
 * backend (OpenCL, PTX). That replacement is performed in OCLLoweringProvider, or
 * PTXLoweringProvider, and drives the {@link ThreadIdFixedWithNextNode} to extend FixedWithNextNode
 * in order to be replaced by a FloatingNode.
 */
@NodeInfo(shortName = "GlobalThreadId")
public class ThreadIdFixedWithNextNode extends FixedWithNextNode implements Lowerable {

  public static final NodeClass<ThreadIdFixedWithNextNode> TYPE =
      NodeClass.create(ThreadIdFixedWithNextNode.class);
  private final int dimension;
  @Input ValueNode object;

  public ThreadIdFixedWithNextNode(ValueNode index, int dimension) {
    super(TYPE, StampFactory.forUnsignedInteger(32));
    this.object = index;
    this.dimension = dimension;
  }

  public ValueNode object() {
    return this.object;
  }

  public int getDimension() {
    return dimension;
  }

  @Override
  public void lower(LoweringTool loweringTool) {
    loweringTool.getLowerer().lower(this, loweringTool);
  }
}
