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

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.api.functions.flowlogic.SetVariable;
import com.machnos.api.gateway.server.domain.api.functions.flowlogic.Stop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Abstract superclass for all test classes that test extensions of the <code>CompoundFunction</code> class.
 */
public abstract class AbstractCompoundFunctionTest<T extends CompoundFunction<T>> {

    /**
     * Gives an instance of the class to test.
     *
      * @return The class to test.
     */
    protected abstract T getInstance();

    /**
     * Test stopping the execution of the function.
     */
    @Test
    public void testStop() {
        var compoundFunction = getInstance()
                .addFunction(new SetVariable().setVariableName("stopped").setVariableValue(false))
                .addFunction(new Stop())
                .addFunction(new SetVariable().setVariableName("stopped").setVariableValue(true));

        var result = compoundFunction.execute(new ExecutionContext(null, null, null));
        assertTrue(result.isStopped());
    }

}
