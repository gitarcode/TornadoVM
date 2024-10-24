/*
 * This file is part of Tornado: A heterogeneous programming framework:
 * https://github.com/beehive-lab/tornadovm
 *
 * Copyright (c) 2021, APT Group, Department of Computer Science,
 * School of Engineering, The University of Manchester. All rights reserved.
 * Copyright (c) 2009-2021, Oracle and/or its affiliates. All rights reserved.
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
package uk.ac.manchester.tornado.drivers.spirv.graal.nodes;

import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.Value;
import org.graalvm.compiler.core.common.LIRKind;
import org.graalvm.compiler.core.common.type.StampFactory;
import org.graalvm.compiler.graph.NodeClass;
import org.graalvm.compiler.lir.Variable;
import org.graalvm.compiler.lir.gen.LIRGeneratorTool;
import org.graalvm.compiler.nodeinfo.NodeInfo;
import org.graalvm.compiler.nodes.ConstantNode;
import org.graalvm.compiler.nodes.calc.FloatingNode;
import org.graalvm.compiler.nodes.spi.LIRLowerable;
import org.graalvm.compiler.nodes.spi.NodeLIRBuilderTool;
import uk.ac.manchester.tornado.drivers.spirv.SPIRVThreadBuiltIn;
import uk.ac.manchester.tornado.drivers.spirv.graal.lir.SPIRVLIRStmt;
import uk.ac.manchester.tornado.drivers.spirv.graal.lir.SPIRVUnary;

@NodeInfo(shortName = "SPIRV-Thread-Size")
public class GlobalThreadSizeNode extends FloatingNode implements LIRLowerable {

  public static final NodeClass<GlobalThreadSizeNode> TYPE =
      NodeClass.create(GlobalThreadSizeNode.class);

  @Input protected ConstantNode dimensionIndex;

  public GlobalThreadSizeNode(ConstantNode dimension) {
    super(TYPE, StampFactory.forKind(JavaKind.Int));
    this.dimensionIndex = dimension;
  }

  @Override
  public void generate(NodeLIRBuilderTool generator) {
    LIRGeneratorTool tool = generator.getLIRGeneratorTool();
    Variable result = tool.newVariable(tool.getLIRKind(stamp));
    Value valueDimension = generator.operand(dimensionIndex);
    LIRKind lirKind = tool.getLIRKind(stamp);
    tool.append(
        new SPIRVLIRStmt.AssignStmt(
            result,
            new SPIRVUnary.ThreadBuiltinCallForSPIRV(
                SPIRVThreadBuiltIn.GLOBAL_SIZE, result, lirKind, valueDimension)));
    generator.setResult(this, result);
  }
}
