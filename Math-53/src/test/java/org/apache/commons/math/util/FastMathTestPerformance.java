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
package org.apache.commons.math.util;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Performance tests for FastMath.
 * Not enabled by default, as the class does not end in Test.
 * 
 * Invoke by running<br/>
 * {@code mvn test -Dtest=FastMathTestPerformance}<br/>
 * or by running<br/>
 * {@code mvn test -Dtest=FastMathTestPerformance -DargLine="-DtestRuns=1234 -server"}<br/>
 */
public class FastMathTestPerformance {
    private static final int RUNS = Integer.parseInt(System.getProperty("testRuns","10000000"));
    private static final double F1 = 1d / RUNS;

    // Header format
    private static final String FMT_HDR = "%-5s %13s %13s %13s Runs=%d Java %s (%s) %s (%s)";
    // Detail format
    private static final String FMT_DTL = "%-5s %6d %6.1f %6d %6.4f %6d %6.4f";

    @BeforeClass
    public static void header() {
        System.out.println(String.format(FMT_HDR,
                "Name","StrictMath","FastMath","Math",RUNS,
                System.getProperty("java.version"),
                System.getProperty("java.runtime.version","?"),
                System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version")
                ));
    }

    private static void report(String name, long strictMathTime, long fastMathTime, long mathTime) {
        long unitTime = strictMathTime;
        System.out.println(String.format(FMT_DTL,
                name,
                strictMathTime / RUNS, (double) strictMathTime / unitTime,
                fastMathTime / RUNS, (double) fastMathTime / unitTime,
                mathTime / RUNS, (double) mathTime / unitTime
                ));
    }

    @Test
    public void testLog() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.log(Math.PI + i/* 1.0 + i/1e9 */);
        long strictMath = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.log(Math.PI + i/* 1.0 + i/1e9 */);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.log(Math.PI + i/* 1.0 + i/1e9 */);
        long mathTime = System.nanoTime() - time;

        report("log",strictMath,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testLog10() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.log10(Math.PI + i/* 1.0 + i/1e9 */);
        long strictMath = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.log10(Math.PI + i/* 1.0 + i/1e9 */);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.log10(Math.PI + i/* 1.0 + i/1e9 */);
        long mathTime = System.nanoTime() - time;

        report("log10",strictMath,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testLog1p() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.log1p(Math.PI + i/* 1.0 + i/1e9 */);
        long strictMath = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.log1p(Math.PI + i/* 1.0 + i/1e9 */);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.log1p(Math.PI + i/* 1.0 + i/1e9 */);
        long mathTime = System.nanoTime() - time;

        report("log1p",strictMath,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testPow() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.pow(Math.PI + i * F1, i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.pow(Math.PI + i * F1, i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.pow(Math.PI + i * F1, i * F1);
        long mathTime = System.nanoTime() - time;
        report("pow",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testExp() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.exp(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.exp(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.exp(i * F1);
        long mathTime = System.nanoTime() - time;

        report("exp",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testSin() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.sin(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.sin(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.sin(i * F1);
        long mathTime = System.nanoTime() - time;

        report("sin",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testAsin() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.asin(i / 10000000.0);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.asin(i / 10000000.0);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.asin(i / 10000000.0);
        long mathTime = System.nanoTime() - time;

        report("asin",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testCos() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.cos(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.cos(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.cos(i * F1);
        long mathTime = System.nanoTime() - time;

        report("cos",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }
            
    @Test
    public void testAcos() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.acos(i / 10000000.0);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.acos(i / 10000000.0);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.acos(i / 10000000.0);
        long mathTime = System.nanoTime() - time;
        report("acos",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testTan() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.tan(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.tan(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.tan(i * F1);
        long mathTime = System.nanoTime() - time;

        report("tan",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testAtan() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.atan(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.atan(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.atan(i * F1);
        long mathTime = System.nanoTime() - time;

        report("atan",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testAtan2() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.atan2(i * F1, i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.atan2(i * F1, i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.atan2(i * F1, i * F1);
        long mathTime = System.nanoTime() - time;

        report("atan2",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testHypot() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.hypot(i * F1, i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.hypot(i * F1, i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.hypot(i * F1, i * F1);
        long mathTime = System.nanoTime() - time;

        report("hypot",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }
     
    @Test
    public void testCbrt() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.cbrt(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.cbrt(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.cbrt(i * F1);
        long mathTime = System.nanoTime() - time;

        report("cbrt",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testSqrt() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.sqrt(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.sqrt(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.sqrt(i * F1);
        long mathTime = System.nanoTime() - time;

        report("sqrt",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testCosh() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.cosh(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.cosh(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.cosh(i * F1);
        long mathTime = System.nanoTime() - time;

        report("cosh",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testSinh() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.sinh(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.sinh(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.sinh(i * F1);
        long mathTime = System.nanoTime() - time;

        report("sinh",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testTanh() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.tanh(i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.tanh(i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.tanh(i * F1);
        long mathTime = System.nanoTime() - time;

        report("tanh",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }
     
    @Test
    public void testExpm1() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.expm1(-i * F1);
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.expm1(-i * F1);
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.expm1(-i * F1);
        long mathTime = System.nanoTime() - time;
        report("expm1",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }

    @Test
    public void testAbs() {
        double x = 0;
        long time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += StrictMath.abs(i * (1 - 0.5 * RUNS));
        long strictTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += FastMath.abs(i * (1 - 0.5 * RUNS));
        long fastTime = System.nanoTime() - time;

        x = 0;
        time = System.nanoTime();
        for (int i = 0; i < RUNS; i++)
            x += Math.abs(i * (1 - 0.5 * RUNS));
        long mathTime = System.nanoTime() - time;

        report("abs",strictTime,fastTime,mathTime);
        Assert.assertTrue(!Double.isNaN(x));
    }
}
