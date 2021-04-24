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

package com.machnos.api.gateway.server.domain.api.variables;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test class for the <code>NumberVariable</code> class.
 */
public class NumberVariableTest extends AbstractVariableTest<NumberVariable, BigDecimal> {

    @Override
    protected NumberVariable getInstance() {
        return new NumberVariable();
    }

    @Override
    protected BigDecimal getValue() {
        return new BigDecimal(getRandom().nextInt()).setScale(getInstance().getScale(), RoundingMode.HALF_UP);
    }

    /**
     * Test setting the scale.
     */
    @Test
    public void testGetSetScale() {
        final var instance = getInstance();
        var value = getValue();

        instance.setValue(value).setScale(5);
        assertNotEquals(value.scale(), instance.getScale());
        assertNotEquals(value, instance.getValue());
        assertEquals(value.setScale(5, RoundingMode.HALF_UP), instance.getValue());
    }

    /**
     * Test setting the precision.
     */
    @Test
    public void testSetPrecision() {
        // Create an instance with a precision of 2.
        final var instance = getInstance().setPrecision(2);
        var value = new BigDecimal(1);
        final var almostOne = getInstance().setValue(new BigDecimal("1.01"));

        instance.setValue(value).add(almostOne);
        assertEquals("2.00", instance.getValue().toString());

        // now set the precision to 3 numbers and test again
        instance.setValue(value).setPrecision(3).add(almostOne);
        assertEquals("2.01", instance.getValue().toString());
    }

    /**
     * Test setting the rounding mode.
     */
    @Test
    public void testSetRoundingMode() {
        final var instance = getInstance().setScale(0);
        final var oneAndAHalf = new BigDecimal("1.5");

        instance.setRoundingMode(RoundingMode.HALF_UP).setValue(oneAndAHalf);
        assertEquals("2", instance.getValue().toString());

        instance.setRoundingMode(RoundingMode.HALF_DOWN).setValue(oneAndAHalf);
        assertEquals("1", instance.getValue().toString());
    }

    /**
     * Test adding a value.
     */
    @Test
    public void testAdd() {
        final var instance = getInstance();
        final var oneAndAHalf = new NumberVariable().setPrecision(1).setValue(new BigDecimal("1.5"));

        instance.add(oneAndAHalf);
        assertEquals("1.50", instance.getValue().toString());
        assertEquals("3.00", instance.add(oneAndAHalf).getValue().toString());
    }

    /**
     * Test subtracting a value.
     */
    @Test
    public void testSubtract() {
        final var instance = getInstance();
        final var oneAndAHalf = new NumberVariable().setPrecision(1).setValue(new BigDecimal("1.5"));

        instance.subtract(oneAndAHalf);
        assertEquals("-1.50", instance.getValue().toString());
        assertEquals("-3.00", instance.subtract(oneAndAHalf).getValue().toString());
    }

    /**
     * Test multiplying a value.
     */
    @Test
    public void testMultiply() {
        final var instance = getInstance();
        final var oneAndAHalf = new NumberVariable().setPrecision(1).setValue(new BigDecimal("1.5"));

        instance.add(oneAndAHalf).multiply(oneAndAHalf);
        assertEquals("2.25", instance.getValue().toString());
    }

    /**
     * Test dividing a value.
     */
    @Test
    public void testDivide() {
        final var instance = getInstance();
        final var ten = new NumberVariable().setValue(new BigDecimal("10"));

        instance.add(ten).divide(new NumberVariable().setValue(new BigDecimal("3")));
        assertEquals("3.33", instance.getValue().toString());
    }
}
