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

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Abstract superclass for the test classes that test the <code>AbstractVariable</code> implementations.
 * @param <I> The instance to test.
 */
public abstract class AbstractVariableTest<I extends AbstractVariable<T, I>, T> {

    /**
     * A random instance to randomize the test cases.
     */
    private final Random random = new Random();

    @Test
    public void testGetSetName() {
        final var instance = getInstance();
        final var name = instance.getClass().getName();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    @Test
    public void testGetSetValue() {
        final var instance = getInstance();
        final var value = getValue();
        instance.setValue(value);
        assertEquals(value, instance.getValue());
    }

    /**
     * Gives a <code>Random</code> instance.
     * @return A <code>Random</code> instance.
     */
    public Random getRandom() {
        return this.random;
    }

    protected abstract I getInstance();
    protected abstract T getValue();
}
