/*
 * Copyright (c) 2020, APT Group, Department of Computer Science,
 * School of Engineering, The University of Manchester. All rights reserved.
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
package uk.ac.manchester.tornado.drivers.ptx.graal.lir;

import static uk.ac.manchester.tornado.api.exceptions.TornadoInternalError.unimplemented;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXBinaryIntrinsic.FLOAT_MAX;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXBinaryIntrinsic.FLOAT_MIN;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXBinaryIntrinsic.INT_MAX;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXBinaryIntrinsic.INT_MIN;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.ABS;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.COS;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.EXP2;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.FLOAT_FLOOR;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.LOG2;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.POPCOUNT;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.SIN;
import static uk.ac.manchester.tornado.drivers.ptx.graal.asm.PTXAssembler.PTXUnaryIntrinsic.SQRT;

import org.graalvm.compiler.core.common.LIRKind;
import org.graalvm.compiler.lir.Variable;

import jdk.vm.ci.meta.Value;
import uk.ac.manchester.tornado.api.exceptions.TornadoInternalError;
import uk.ac.manchester.tornado.drivers.ptx.common.PTXLogger;

public class PTXBuiltinTool {

    public Value genFloatACos(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatACosh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatACospi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatASin(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatASinh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatASinpi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatATan(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatATanh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatATanpi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatCbrt(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatCeil(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatCos(Value input) {
        PTXLogger.traceBuildLIR("genCos: cos(%s)", input);
        return new PTXUnary.Intrinsic(COS, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatCosh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatCospi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatErfc(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatErf(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatExp2(Value input) {
        PTXLogger.traceBuildLIR("genExp: exp(%s)", input);
        return new PTXUnary.Intrinsic(EXP2, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatFloor(Value input) {
        PTXLogger.traceBuildLIR("genFloatFloor: floor(%s)", input);
        return new PTXUnary.Intrinsic(FLOAT_FLOOR, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatILogb(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatLGamma(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatLog2(Value input) {
        PTXLogger.traceBuildLIR("genFloatLog2: input=%s", input);
        return new PTXUnary.Intrinsic(LOG2, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatLog10(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatLog1p(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatLogb(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatNan(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatRint(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatRound(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatRSqrt(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatSin(Value input) {
        PTXLogger.traceBuildLIR("genSin: sin(%s)", input);
        return new PTXUnary.Intrinsic(SIN, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatSinh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatSinpi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatTan(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatTanh(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatTanpi(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatTGamma(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatTrunc(Value input) {
        unimplemented();
        return null;
    }

    public Value genFloatATan2(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatATan2pi(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatCopySign(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatDim(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatFma(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatMax(Value x, Value y) {
        PTXLogger.traceBuildLIR("genFloatMax: max(%s,%s)", x, y);
        return new PTXBinary.Intrinsic(FLOAT_MAX, LIRKind.combine(x, y), x, y);
    }

    public Value genFloatMin(Value x, Value y) {
        PTXLogger.traceBuildLIR("genFloatMin: min(%s,%s)", x, y);
        return new PTXBinary.Intrinsic(FLOAT_MIN, LIRKind.combine(x, y), x, y);
    }

    public Value genFloatMod(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatFract(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatFrexp(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatHypot(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatLdexp(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatMad(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatMaxmag(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatMinmag(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatModf(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatNextAfter(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatPow(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatPown(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatPowr(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatRemainder(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatRootn(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genFloatSincos(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Variable genBitCount(Value input) {
        unimplemented();
        return null;
    }

    public Variable genBitScanForward(Value input) {
        unimplemented();
        return null;
    }

    public Variable genBitScanReverse(Value input) {
        unimplemented();
        return null;
    }

    public Value genIntAbs(Value input) {
        PTXLogger.traceBuildLIR("genIntAbs: abs(%s)", input);
        return new PTXUnary.Intrinsic(ABS, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatSqrt(Value input) {
        PTXLogger.traceBuildLIR("genAbs: sqrt(%s)", input);
        return new PTXUnary.Intrinsic(SQRT, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genIntMax(Value x, Value y) {
        PTXLogger.traceBuildLIR("genMax: max(%s,%s)", x, y);
        return new PTXBinary.Intrinsic(INT_MAX, LIRKind.combine(x, y), x, y);
    }

    public Value genIntMin(Value x, Value y) {
        PTXLogger.traceBuildLIR("genMin: min(%s,%s)", x, y);
        return new PTXBinary.Intrinsic(INT_MIN, LIRKind.combine(x, y), x, y);
    }

    public Value genIntClz(Value value) {
        unimplemented();
        return null;
    }

    public Value genIntPopcount(Value value) {
        PTXLogger.traceBuildLIR("genBitCount: bitcount(%s)", value);
        return new PTXUnary.Intrinsic(POPCOUNT, LIRKind.value(value.getPlatformKind()), value);
    }

    public Value genIntClamp(Value x, Value y, Value z) {
        TornadoInternalError.unimplemented();
        return null;
    }

    public Value genIntMad24(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genIntMadHi(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genIntMadSat(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genFloatAbs(Value input) {
        PTXLogger.traceBuildLIR("genFloatAbs: abs(%s)", input);
        return new PTXUnary.Intrinsic(ABS, LIRKind.value(input.getPlatformKind()), input);
    }

    public Value genFloatFMA(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genFloatMAD(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genFloatRemquo(Value x, Value y, Value z) {
        unimplemented();
        return null;
    }

    public Value genGeometricDot(Value x, Value y) {
        unimplemented();
        return null;
    }

    public Value genGeometricCross(Value x, Value y) {
        unimplemented();
        return null;
    }

}
