/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math.analysis;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.function.Identity;
import org.apache.commons.math.analysis.function.Constant;
import org.apache.commons.math.analysis.function.Minus;
import org.apache.commons.math.analysis.function.Inverse;
import org.apache.commons.math.analysis.function.Power;
import org.apache.commons.math.analysis.function.Sin;
import org.apache.commons.math.analysis.function.Sinc;
import org.apache.commons.math.analysis.function.Cos;
import org.apache.commons.math.analysis.function.Cosh;
import org.apache.commons.math.analysis.BivariateRealFunction;
import org.apache.commons.math.analysis.function.Add;
import org.apache.commons.math.analysis.function.Multiply;
import org.apache.commons.math.analysis.function.Divide;
import org.apache.commons.math.analysis.function.Min;
import org.apache.commons.math.analysis.function.Max;
import org.apache.commons.math.analysis.function.Pow;
import org.apache.commons.math.analysis.function.Log;
import org.apache.commons.math.analysis.MultivariateFunction;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link FunctionUtils}.
 */
public class FunctionUtilsTest {
    private final double EPS = Math.ulp(1d);

    @Test
    public void testCompose() {
        UnivariateFunction id = new Identity();
        Assert.assertEquals(3, FunctionUtils.compose(id, id, id).value(3), EPS);

        UnivariateFunction c = new Constant(4);
        Assert.assertEquals(4, FunctionUtils.compose(id, c).value(3), EPS);
        Assert.assertEquals(4, FunctionUtils.compose(c, id).value(3), EPS);

        UnivariateFunction m = new Minus();
        Assert.assertEquals(-3, FunctionUtils.compose(m).value(3), EPS);
        Assert.assertEquals(3, FunctionUtils.compose(m, m).value(3), EPS);

        UnivariateFunction inv = new Inverse();
        Assert.assertEquals(-0.25, FunctionUtils.compose(inv, m, c, id).value(3), EPS);

        UnivariateFunction pow = new Power(2);
        Assert.assertEquals(81, FunctionUtils.compose(pow, pow).value(3), EPS);
    }

    @Test
    public void testComposeDifferentiable() {
        DifferentiableUnivariateFunction id = new Identity();
        Assert.assertEquals(1, FunctionUtils.compose(id, id, id).derivative().value(3), EPS);

        DifferentiableUnivariateFunction c = new Constant(4);
        Assert.assertEquals(0, FunctionUtils.compose(id, c).derivative().value(3), EPS);
        Assert.assertEquals(0, FunctionUtils.compose(c, id).derivative().value(3), EPS);

        DifferentiableUnivariateFunction m = new Minus();
        Assert.assertEquals(-1, FunctionUtils.compose(m).derivative().value(3), EPS);
        Assert.assertEquals(1, FunctionUtils.compose(m, m).derivative().value(3), EPS);

        DifferentiableUnivariateFunction inv = new Inverse();
        Assert.assertEquals(0.25, FunctionUtils.compose(inv, m, id).derivative().value(2), EPS);

        DifferentiableUnivariateFunction pow = new Power(2);
        Assert.assertEquals(108, FunctionUtils.compose(pow, pow).derivative().value(3), EPS);

        DifferentiableUnivariateFunction log = new Log();
        double a = 9876.54321;
        Assert.assertEquals(pow.derivative().value(a) / pow.value(a),
                            FunctionUtils.compose(log, pow).derivative().value(a), EPS);
    }

    @Test
    public void testAdd() {
        UnivariateFunction id = new Identity();
        UnivariateFunction c = new Constant(4);
        UnivariateFunction m = new Minus();
        UnivariateFunction inv = new Inverse();

        Assert.assertEquals(4.5, FunctionUtils.add(inv, m, c, id).value(2), EPS);
        Assert.assertEquals(4 + 2, FunctionUtils.add(c, id).value(2), EPS);
        Assert.assertEquals(4 - 2, FunctionUtils.add(c, FunctionUtils.compose(m, id)).value(2), EPS);
    }

    @Test
    public void testAddDifferentiable() {
        DifferentiableUnivariateFunction sin = new Sin();
        DifferentiableUnivariateFunction c = new Constant(4);
        DifferentiableUnivariateFunction m = new Minus();
        DifferentiableUnivariateFunction inv = new Inverse();

        final double a = 123.456;
        Assert.assertEquals(- 1 / (a * a) -1 + Math.cos(a),
                            FunctionUtils.add(inv, m, c, sin).derivative().value(a),
                            EPS);
    }

    @Test
    public void testMultiply() {
        UnivariateFunction c = new Constant(4);
        Assert.assertEquals(16, FunctionUtils.multiply(c, c).value(12345), EPS);

        UnivariateFunction inv = new Inverse();
        UnivariateFunction pow = new Power(2);
        Assert.assertEquals(1, FunctionUtils.multiply(FunctionUtils.compose(inv, pow), pow).value(3.5), EPS);
    }

    @Test
    public void testMultiplyDifferentiable() {
        DifferentiableUnivariateFunction c = new Constant(4);
        DifferentiableUnivariateFunction id = new Identity();
        final double a = 1.2345678;
        Assert.assertEquals(8 * a, FunctionUtils.multiply(c, id, id).derivative().value(a), EPS);

        DifferentiableUnivariateFunction inv = new Inverse();
        DifferentiableUnivariateFunction pow = new Power(2.5);
        DifferentiableUnivariateFunction cos = new Cos();
        Assert.assertEquals(1.5 * Math.sqrt(a) * Math.cos(a) - Math.pow(a, 1.5) * Math.sin(a),
                            FunctionUtils.multiply(inv, pow, cos).derivative().value(a), EPS);

        DifferentiableUnivariateFunction cosh = new Cosh();
        Assert.assertEquals(1.5 * Math.sqrt(a) * Math.cosh(a) + Math.pow(a, 1.5) * Math.sinh(a),
                            FunctionUtils.multiply(inv, pow, cosh).derivative().value(a), 8 * EPS);
    }

    @Test
    public void testCombine() {
        BivariateRealFunction bi = new Add();
        UnivariateFunction id = new Identity();
        UnivariateFunction m = new Minus();
        UnivariateFunction c = FunctionUtils.combine(bi, id, m);
        Assert.assertEquals(0, c.value(2.3456), EPS);

        bi = new Multiply();
        UnivariateFunction inv = new Inverse();
        c = FunctionUtils.combine(bi, id, inv);
        Assert.assertEquals(1, c.value(2.3456), EPS);
    }

    @Test
    public void testCollector() {
        BivariateRealFunction bi = new Add();
        MultivariateFunction coll = FunctionUtils.collector(bi, 0);
        Assert.assertEquals(10, coll.value(new double[] {1, 2, 3, 4}), EPS);

        bi = new Multiply();
        coll = FunctionUtils.collector(bi, 1);
        Assert.assertEquals(24, coll.value(new double[] {1, 2, 3, 4}), EPS);

        bi = new Max();
        coll = FunctionUtils.collector(bi, Double.NEGATIVE_INFINITY);
        Assert.assertEquals(10, coll.value(new double[] {1, -2, 7.5, 10, -24, 9.99}), 0);

        bi = new Min();
        coll = FunctionUtils.collector(bi, Double.POSITIVE_INFINITY);
        Assert.assertEquals(-24, coll.value(new double[] {1, -2, 7.5, 10, -24, 9.99}), 0);
    }

    @Test
    public void testSinc() {
        BivariateRealFunction div = new Divide();
        UnivariateFunction sin = new Sin();
        UnivariateFunction id = new Identity();
        UnivariateFunction sinc1 = FunctionUtils.combine(div, sin, id);
        UnivariateFunction sinc2 = new Sinc();

        for (int i = 0; i < 10; i++) {
            double x = Math.random();
            Assert.assertEquals(sinc1.value(x), sinc2.value(x), EPS);
        }
    }

    @Test
    public void testFixingArguments() {
        UnivariateFunction scaler = FunctionUtils.fix1stArgument(new Multiply(), 10);
        Assert.assertEquals(1.23456, scaler.value(0.123456), EPS);

        UnivariateFunction pow1 = new Power(2);
        UnivariateFunction pow2 = FunctionUtils.fix2ndArgument(new Pow(), 2);

        for (int i = 0; i < 10; i++) {
            double x = Math.random() * 10;
            Assert.assertEquals(pow1.value(x), pow2.value(x), 0);
        }
    }
}
