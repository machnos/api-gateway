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

package com.machnos.api.gateway.server.domain.api.functions.flowlogic;

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the <code>SetVariable</code> class.
 */
public class SetVariableTest {

    /**
     * Test getting and setting the name.
     */
    @Test
    public void testGetSetName() {
        final var name = getClass().getName();
        final var function = new SetVariable();
        function.setVariableName(name);
        assertEquals(name, function.getVariableName());
    }

    /**
     * Test getting and setting the value.
     */
    @Test
    public void testGetSetValue() {
        final var value = new Object();
        final var function = new SetVariable();
        function.setVariableValue(value);
        assertEquals(value, function.getVariableValue());
    }

    /**
     * Test executing the function, without setting the name.
     */
    @Test
    public void testExecuteWithoutName() {
        final var function = new SetVariable();
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isFailed());
    }

    /**
     * Test setting a plain value.
     */
    @Test
    public void testExecuteWithPlainValue() {
        final var varName = "Test";
        final var varValue = "TestValue";
        final var function = new SetVariable().setVariableName(varName).setVariableValue(varValue);
        final var executionContext = new ExecutionContext(null, null, null);
        executionContext.getVariables().startScope();
        var result = function.execute(executionContext);
        assertTrue(result.isSucceeded());
        assertEquals(varValue, executionContext.getVariable(varName));
        executionContext.getVariables().endScope();
    }

    /**
     * Test setting a variable value.
     */
    @Test
    public void testExecuteWithVariableValue() {
        final var varName = "Test";
        final var varValue = "TestValue";
        final var varNameWithVariable = ("VarNameWithVariable");
        final var varValueWithVariable = ("${test}");
        final var function = new SetVariable().setVariableName(varNameWithVariable).setVariableValue(varValueWithVariable);
        final var executionContext = new ExecutionContext(null, null, null);
        executionContext.getVariables().startScope();
        executionContext.getVariables().setVariable(varName, varValue);
        var result = function.execute(executionContext);
        assertTrue(result.isSucceeded());
        assertEquals(varValue, executionContext.getVariable(varNameWithVariable));
        executionContext.getVariables().endScope();
    }

    /**
     * Test setting a variable name.
     */
    @Test
    public void testExecuteWithVariableName() {
        final var varName = "Test";
        final var varValue = "TestValue";
        final var varNameWithVariable = ("${test}");
        final var varValueWithVariable = ("AnotherValue");
        final var function = new SetVariable().setVariableName(varNameWithVariable).setVariableValue(varValueWithVariable);
        final var executionContext = new ExecutionContext(null, null, null);
        executionContext.getVariables().startScope();
        executionContext.getVariables().setVariable(varName, varValue);
        var result = function.execute(executionContext);
        assertTrue(result.isSucceeded());
        assertEquals(varValueWithVariable, executionContext.getVariable(varValue));
        executionContext.getVariables().endScope();
    }

}
