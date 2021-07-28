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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for the <code>Variables</code> class.
 */
public class VariablesTest {

    /**
     * Test getting variables.
     */
    @Test
    public void testAddGetVariable() {
        final var variables = new Variables();
        final var varName = "Name";
        assertNull(variables.getVariable(varName));
        variables.startScope();
        variables.setVariable(varName, "John");
        assertEquals("John", variables.getVariable(varName));
        variables.setVariable(varName, "Doe");
        assertEquals("Doe", variables.getVariable(varName));
        variables.endScope();
        assertNull(variables.getVariable(varName));
    }

    /**
     * Test getting variables when using multiple scopes.
     */
    @Test
    public void testMultiScopes() {
        final var variables = new Variables();
        final var varNameLevel1 = "level1";
        final var varNameLevel2 = "level2";
        // Start level 1
        variables.startScope();
        variables.setVariable(varNameLevel1, "value1");
        // Start level 2
        variables.startScope();
        variables.setVariable(varNameLevel2, "value2");
        // Both variables should be available.
        assertEquals(variables.getVariable(varNameLevel1), "value1");
        assertEquals(variables.getVariable(varNameLevel2), "value2");
        // Now change both variables.
        variables.setVariable(varNameLevel1, "value1Changed");
        variables.setVariable(varNameLevel2, "value2Changed");
        assertEquals(variables.getVariable(varNameLevel1), "value1Changed");
        assertEquals(variables.getVariable(varNameLevel2), "value2Changed");
        // End level 2. The variable added in this level should no longer be available.
        variables.endScope();
        assertEquals(variables.getVariable(varNameLevel1), "value1Changed");
        assertNull(variables.getVariable(varNameLevel2));
    }

    /**
     * Test setting and getting a variable without a name. This should do nothing.
     */
    @Test
    public void testGetSetVariableWithNullName() {
        final var variables = new Variables();
        variables.setVariable(null, new Object());
        assertNull(variables.getVariable(null));
    }
}
