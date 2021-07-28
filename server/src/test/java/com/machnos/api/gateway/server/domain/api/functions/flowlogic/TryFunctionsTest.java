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
import com.machnos.api.gateway.server.domain.api.functions.AbstractCompoundFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the <code>TryFunctions</code> class.
 */
public class TryFunctionsTest extends AbstractCompoundFunctionTest<TryFunctions> {

    @Override
    protected TryFunctions getInstance() {
        return new TryFunctions();
    }

    /**
     * Test the execution of the function with only succeeding child functions.
     */
    @Test
    public void testChildFunctionsThatSucceed() {
        final var function = getInstance().addFunction(new Continue()).addFunction(new Continue());
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isSucceeded());
    }

    /**
     * Test the execution of the function with only succeeding child functions.
     */
    @Test
    public void testChildFunctionsThatFail() {
        final var function = getInstance().addFunction(new Fail());
        var result = function.execute(new ExecutionContext(null, null, null));
        // We still expect success.
        assertTrue(result.isSucceeded());
    }

    /**
     * Test the successful execution of the error branch.
     */
    @Test
    public void testSuccessfulErrorBranch() {
        final var varName = "Test";
        final var varValue = "TestValue";
        final var function = getInstance().addFunction(new Fail());
        function.addErrorFunction(new SetVariable().setVariableName(varName).setVariableValue(varValue));
        final var executionContext = new ExecutionContext(null, null, null);
        executionContext.getVariables().startScope();
        // The TryFunctions has its own variable scope, so we need to initialize the variable at a higher level outside the function.
        executionContext.getVariables().setVariable(varName, "DummyValue");
        var result = function.execute(executionContext);
        assertTrue(result.isSucceeded());
        assertEquals(varValue, executionContext.getVariable(varName));
        executionContext.getVariables().endScope();
    }

    /**
     * Test the failure of the error branch.
     */
    @Test
    public void testFailingErrorBranch() {
        final var function = getInstance().addFunction(new Fail());
        function.addErrorFunction(new Fail());
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isFailed());
    }

    /**
     * Test a stop result in the error branch.
     */
    @Test
    public void testStoppingErrorBranch() {
        final var function = getInstance().addFunction(new Fail());
        function.addErrorFunction(new Stop());
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isStopped());
    }
}
