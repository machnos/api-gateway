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

import com.machnos.api.gateway.server.domain.api.Api;
import com.machnos.api.gateway.server.domain.api.MockApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the <code>ApiVariableHandler</code> class.
 */
public class ApiVariableHandlerTest extends AbstractVariableHandlerTest<ApiVariableHandler, Api> {

    @Override
    protected ApiVariableHandler getHandlerInstance() {
        return new ApiVariableHandler();
    }

    @Override
    protected MockApi getObjectToHandle() {
        return new MockApi();
    }

    /**
     * Test getting the username.
     */
    @Test
    public void testGetName() {
        final var variableHandler = getHandlerInstance();
        final var api = getObjectToHandle();
        final var name = "DummyApiName";
        api.setName(name);
        assertEquals(name, variableHandler.getValue(ApiVariableHandler.NAME, api));
    }

    /**
     * Test getting the context root
     */
    @Test
    public void testGetContextRoot() {
        final var variableHandler = getHandlerInstance();
        final var api = getObjectToHandle();
        final var contextRoot = "/api/dummy";
        api.setContextRoot(contextRoot);
        assertEquals(contextRoot, variableHandler.getValue(ApiVariableHandler.CONTEXT_ROOT, api));
    }

}