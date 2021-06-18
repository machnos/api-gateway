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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Abstract superclass for all <code>VariableHandler</code> testcases.
 *
 * @param <T> The <code>VariableHandler</code> implementation to test.
 * @param <U> The class that is handled by the <code>VariableHandler</code> implementation.
 */
public abstract class AbstractVariableHandlerTest<T extends VariableHandler<U>, U> {

    /**
     * Gives an instance of the <code>VariableHandler</code> that needs to be tested.
     *
     * @return The <code>VariableHandler</code>
     */
    protected abstract T getHandlerInstance();

    /**
     * Gives an instance of the object that is handled by the <code>VariableHandler</code> implementation.
     *
     * @return An instance of the object that is handled.
     */
    protected abstract U getObjectToHandle();

    /**
     * Test the {@link AbstractVariableHandler#NO_VARIABLE} retrieval.
     */
    @Test
    public void testNoValue() {
        final var variableHandler  = getHandlerInstance();
        final var handledObject = getObjectToHandle();
        assertSame(handledObject, variableHandler.getValue(AbstractVariableHandler.NO_VARIABLE, handledObject));
    }

    /**
     * Test the behaviour of retrieving a variable without supplying a variable name.
     */
    @Test
    public void testNullVariableName() {
        final var variableHandler  = getHandlerInstance();
        final var handledObject = getObjectToHandle();
        assertNull(variableHandler.getValue(null, handledObject));
    }

}
