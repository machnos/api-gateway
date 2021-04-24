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

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the <code>StringVariable</code> class.
 */
public class StringVariableTest extends AbstractVariableTest<StringVariable, String> {

    @Override
    protected StringVariable getInstance() {
        return new StringVariable();
    }

    @Override
    protected String getValue() {
        final var array = new byte[50];
        getRandom().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * Test the startsWith function.
     */
    @Test
    public void testStartsWith() {
        final var instance = getInstance();
        final var value = getValue();
        assertTrue(instance.setValue(value).startsWith(value.substring(0, getRandom().nextInt(value.length() - 1) + 1)));
        assertFalse(instance.setValue(value).startsWith(value.substring(1, 5)));
        assertFalse(instance.setValue(null).startsWith(value));
    }

    /**
     * Test the endsWith function.
     */
    @Test
    public void testEndsWith() {
        final var instance = getInstance();
        final var value = getValue();
        assertTrue(instance.setValue(value).endsWith(value.substring(getRandom().nextInt(value.length() - 1))));
        assertFalse(instance.setValue(value).endsWith(value.substring(0, 5)));
        assertFalse(instance.setValue(null).endsWith(value));
    }

    /**
     * Test the getLength function.
     */
    @Test
    public void testGetLength() {
        final var instance = getInstance();
        final var value = getValue();
        assertEquals(value.length(), instance.setValue(value).getLength());
        assertEquals(0, instance.setValue(null).getLength());
    }
}
