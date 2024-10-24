/*
 * Copyright (c) 2020, APT Group, Department of Computer Science,
 * School of Engineering, The University of Manchester. All rights reserved.
 * Copyright (c) 2018, 2020, APT Group, Department of Computer Science,
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
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package uk.ac.manchester.tornado.runtime.graal.compiler;

import jdk.vm.ci.code.TargetDescription;
import jdk.vm.ci.meta.ResolvedJavaMethod;
import org.graalvm.compiler.api.replacements.SnippetReflectionProvider;
import org.graalvm.compiler.bytecode.BytecodeProvider;
import org.graalvm.compiler.java.GraphBuilderPhase;
import org.graalvm.compiler.nodes.graphbuilderconf.GraphBuilderConfiguration;
import org.graalvm.compiler.nodes.graphbuilderconf.IntrinsicContext;
import org.graalvm.compiler.phases.OptimisticOptimizations;
import org.graalvm.compiler.phases.util.Providers;
import org.graalvm.compiler.printer.GraalDebugHandlersFactory;
import org.graalvm.compiler.replacements.ReplacementsImpl;

public class TornadoReplacements extends ReplacementsImpl {

  public TornadoReplacements(
      GraalDebugHandlersFactory graalDebugHandlersFactory,
      Providers providers,
      SnippetReflectionProvider snippetReflection,
      BytecodeProvider bytecodeProvider,
      TargetDescription target) {
    super(graalDebugHandlersFactory, providers, snippetReflection, bytecodeProvider, target);
  }

  @Override
  protected GraphMaker createGraphMaker(
      ResolvedJavaMethod substitute, ResolvedJavaMethod original) {
    return new GraphMaker(this, substitute, original) {

      @Override
      protected GraphBuilderPhase.Instance createGraphBuilder(
          Providers providers1,
          GraphBuilderConfiguration graphBuilderConfig,
          OptimisticOptimizations optimisticOpts,
          IntrinsicContext initialIntrinsicContext) {
        return new GraphBuilderPhase.Instance(
            providers1, graphBuilderConfig, optimisticOpts, initialIntrinsicContext);
      }
    };
  }

  @Override
  public void notifyBeforeInline(ResolvedJavaMethod methodToInline) {
    super.notifyBeforeInline(methodToInline);
  }

  @Override
  public void notifyAfterInline(ResolvedJavaMethod methodToInline) {
    super.notifyAfterInline(methodToInline);
  }

  @Override
  public void closeSnippetRegistration() {
    super.closeSnippetRegistration();
  }
}
