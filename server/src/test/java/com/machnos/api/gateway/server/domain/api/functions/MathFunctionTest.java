/*
 * Licensed to Machnos under one or more contributor license
 * agreements. Machnos licenses this file to you under the
 * Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.machnos.api.gateway.server.domain.api.functions;

import com.machnos.api.gateway.server.domain.api.variables.NumberVariable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test class for the <code>MathFunction</code> class.
 */
public class MathFunctionTest {

    /**
     * Test the addition of two <code>NumberVariables</code>.
     */
    @Test
    public void testAdd() {
        final var one = new NumberVariable().setName("one").setValue(new BigDecimal(1));
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));
        final var three = new NumberVariable().setName("three").setValue(new BigDecimal(3));

        Result result = MathFunction.add(one, two, "threeToTest");
        assertFalse(result.hasExceptions());
        assertEquals(three.getValue(), result.getOutputVariables().getNumberVariable("threeToTest").getValue());

        result = MathFunction.add(one, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(three.getValue(), result.getOutputVariables().getNumberVariable(one.getName()).getValue());
    }

    /**
     * Test the subtraction of two <code>NumberVariables</code>.
     */
    @Test
    public void testSubtract() {
        final var one = new NumberVariable().setName("one").setValue(new BigDecimal(1));
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));
        final var minusOne = new NumberVariable().setName("minusOne").setValue(new BigDecimal(-1));

        var result = MathFunction.subtract(one, two, "minusOneToTest");
        assertFalse(result.hasExceptions());
        assertEquals(minusOne.getValue(), result.getOutputVariables().getNumberVariable("minusOneToTest").getValue());

        result = MathFunction.subtract(one, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(minusOne.getValue(), result.getOutputVariables().getNumberVariable(one.getName()).getValue());
    }

    /**
     * Test the multiplicity of two <code>NumberVariables</code>.
     */
    @Test
    public void testMultiply() {
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));
        final var four = new NumberVariable().setName("four").setValue(new BigDecimal(4));

        var result = MathFunction.multiply(two, two, "fourToTest");
        assertFalse(result.hasExceptions());
        assertEquals(four.getValue(), result.getOutputVariables().getNumberVariable("fourToTest").getValue());

        result = MathFunction.multiply(two, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(four.getValue(), result.getOutputVariables().getNumberVariable(two.getName()).getValue());
    }

    /**
     * Test the division of two <code>NumberVariables</code>.
     */
    @Test
    public void testDivide() {
        final var five = new NumberVariable().setName("five").setValue(new BigDecimal(5));
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));
        final var twoAndAHalf = new NumberVariable().setName("twoAndAHalf").setValue(new BigDecimal("2.5"));

        var result = MathFunction.divide(five, two, "twoAndAHalfToTest");
        assertFalse(result.hasExceptions());
        assertEquals(twoAndAHalf.getValue(), result.getOutputVariables().getNumberVariable("twoAndAHalfToTest").getValue());

        result = MathFunction.divide(five, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(twoAndAHalf.getValue(), result.getOutputVariables().getNumberVariable(five.getName()).getValue());
    }

    /**
     * Test the absolute calculation of a <code>NumberVariables</code>.
     */
    @Test
    public void testAbsolute() {
        final var minusTwelvePointZeroThree = new NumberVariable().setName("minusTwelvePointZeroThree").setValue(new BigDecimal(-12.03));
        final var twelvePointZeroThree = new NumberVariable().setName("twelvePointZeroThree").setValue(new BigDecimal(12.03));

        var result = MathFunction.absolute(minusTwelvePointZeroThree,"numberToTest");
        assertFalse(result.hasExceptions());
        assertEquals(twelvePointZeroThree.getValue(), result.getOutputVariables().getNumberVariable("numberToTest").getValue());

        result = MathFunction.absolute(minusTwelvePointZeroThree, null);
        assertFalse(result.hasExceptions());
        assertEquals(twelvePointZeroThree.getValue(), result.getOutputVariables().getNumberVariable(minusTwelvePointZeroThree.getName()).getValue());
    }

    /**
     * Test retrieving the maximum of two <code>NumberVariables</code>.
     */
    @Test
    public void testMaximum() {
        final var five = new NumberVariable().setName("five").setValue(new BigDecimal(5));
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));

        var result = MathFunction.maximum(five, two, "fiveToTest");
        assertFalse(result.hasExceptions());
        assertEquals(five.getValue(), result.getOutputVariables().getNumberVariable("fiveToTest").getValue());

        result = MathFunction.maximum(five, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(five.getValue(), result.getOutputVariables().getNumberVariable(five.getName()).getValue());
    }

    /**
     * Test retrieving the minimim of two <code>NumberVariables</code>.
     */
    @Test
    public void testMinimum() {
        final var five = new NumberVariable().setName("five").setValue(new BigDecimal(5));
        final var two = new NumberVariable().setName("two").setValue(new BigDecimal(2));

        var result = MathFunction.minimum(five, two, "twoToTest");
        assertFalse(result.hasExceptions());
        assertEquals(two.getValue(), result.getOutputVariables().getNumberVariable("twoToTest").getValue());

        result = MathFunction.minimum(five, two, null);
        assertFalse(result.hasExceptions());
        assertEquals(two.getValue(), result.getOutputVariables().getNumberVariable(five.getName()).getValue());
    }
}
