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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the <code>AllFunctionsMustSucceed</code> class.
 */
public class AllFunctionsMustSucceedTest extends AbstractCompoundFunctionTest<AllFunctionsMustSucceed> {

    @Override
    protected AllFunctionsMustSucceed getInstance() {
        return new AllFunctionsMustSucceed();
    }

    /**
     * Test the outcome when a child function fails.
     */
    @Test
    public void testChildFunctionFails() {
        var function = getInstance()
                .addFunction(new SetVariable().setVariableName("test").setVariableValue(true))
                .addFunction(new Fail());
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isFailed());
        // The cause should be the fail result.
        assertNotNull(result.getCause());
    }

    /**
     * Test the outcome when all functions succeed.
     */
    @Test
    public void testAllFunctionsSucceed() {
        var function = getInstance()
                .addFunction(new SetVariable().setVariableName("test1").setVariableValue(true))
                .addFunction(new SetVariable().setVariableName("test2").setVariableValue(true))
                .addFunction(new SetVariable().setVariableName("test3").setVariableValue(true));
        var result = function.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isSucceeded());
    }
}
