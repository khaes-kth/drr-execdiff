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
package org.apache.commons.math.stat.descriptive.moment;

import java.io.Serializable;

import org.apache.commons.math.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math.stat.descriptive.summary.Sum;

/**
 * <p>Computes the arithmetic mean of a set of values. Uses the definitional 
 * formula:</p>
 * <p>
 * mean = sum(x_i) / n
 * </p>
 * <p>where <code>n</code> is the number of observations.
 * </p>
 * <p>When {@link #increment(double)} is used to add data incrementally from a
 * stream of (unstored) values, the value of the statistic that 
 * {@link #getResult()} returns is computed using the following recursive
 * updating algorithm: </p>
 * <ol>
 * <li>Initialize <code>m = </code> the first value</li>
 * <li>For each additional value, update using <br>
 *   <code>m = m + (new value - m) / (number of observations)</code></li>
 * </ol>
 * <p> If {@link #evaluate(double[])} is used to compute the mean of an array
 * of stored values, a two-pass, corrected algorithm is used, starting with
 * the definitional formula computed using the array of stored values and then
 * correcting this by adding the mean deviation of the data values from the
 * arithmetic mean. See, e.g. "Comparison of Several Algorithms for Computing
 * Sample Means and Variances," Robert F. Ling, Journal of the American
 * Statistical Association, Vol. 69, No. 348 (Dec., 1974), pp. 859-866. </p>
 * <p>
 *  Returns <code>Double.NaN</code> if the dataset is empty.
 * </p>
 * <strong>Note that this implementation is not synchronized.</strong> If 
 * multiple threads access an instance of this class concurrently, and at least
 * one of the threads invokes the <code>increment()</code> or 
 * <code>clear()</code> method, it must be synchronized externally.
 * 
 * @version $Revision$ $Date$
 */
public class Mean extends AbstractStorelessUnivariateStatistic 
    implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -1296043746617791564L;    
    
    /** First moment on which this statistic is based. */
    protected FirstMoment moment;

    /** 
     * Determines whether or not this statistic can be incremented or cleared.
     * <p>
     * Statistics based on (constructed from) external moments cannot
     * be incremented or cleared.</p>
     */
    protected boolean incMoment;

    /** Constructs a Mean. */
    public Mean() {
        incMoment = true;
        moment = new FirstMoment();
    }

    /**
     * Constructs a Mean with an External Moment.
     * 
     * @param m1 the moment
     */
    public Mean(final FirstMoment m1) {
        this.moment = m1;
        incMoment = false;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#increment(double)
     */
    public void increment(final double d) {
        if (incMoment) {
            moment.increment(d);
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#clear()
     */
    public void clear() {
        if (incMoment) {
            moment.clear();
        }
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getResult()
     */
    public double getResult() {
        return moment.m1;
    }

    /**
     * @see org.apache.commons.math.stat.descriptive.StorelessUnivariateStatistic#getN()
     */
    public long getN() {
        return moment.getN();
    }

    /**
     * Returns the arithmetic mean of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     * <p>
     * See {@link Mean} for details on the computing algorithm.</p>
     * 
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the mean of the values or Double.NaN if length = 0
     * @throws IllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    public double evaluate(final double[] values,final int begin, final int length) {
        if (test(values, begin, length)) {
            Sum sum = new Sum();
            double sampleSize = (double) length;
            
            // Compute initial estimate using definitional formula
            double xbar = sum.evaluate(values, begin, length) / sampleSize;
            
            // Compute correction factor in second pass
            double correction = 0;
            for (int i = begin; i < begin + length; i++) {
                correction += (values[i] - xbar);
            }
            return xbar + (correction/sampleSize);
        }
        return Double.NaN;
    }
}