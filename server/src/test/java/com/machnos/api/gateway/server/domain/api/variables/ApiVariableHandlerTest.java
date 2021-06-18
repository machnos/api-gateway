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
import com.machnos.api.gateway.server.domain.api.ExecutionContext;
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
    protected DummyApi getObjectToHandle() {
        return new DummyApi();
    }

    /**
     * Test getting the username.
     */
    @Test
    public void testGetName() {
        final var variableHanlder = getHandlerInstance();
        final var api = getObjectToHandle();
        api.setName("DummyApiName");
        assertEquals(api.getName(), variableHanlder.getValue(ApiVariableHandler.NAME, api));
    }

    /**
     * Test getting the context root
     */
    @Test
    public void testGetContextRoot() {
        final var variableHanlder = getHandlerInstance();
        final var api = getObjectToHandle();
        api.setContextRoot("/api/dummy");
        assertEquals(api.getContextRoot(), variableHanlder.getValue(ApiVariableHandler.CONTEXT_ROOT, api));
    }

    /**
     * Dummy <code>Api</code> implementation with setter methods for all properties.
     */
    private static class DummyApi implements Api {

        /**
         * The name of the <code>Api</code>.
         */
        private String name;
        /**
         * The context root of the <code>Api</code>.
         */
        private String contextRoot;

        @Override
        public String getName() {
            return this.name;
        }

        /**
         * Sets the name of the <code>Api</code>.
         *
         * @param name The name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getContextRoot() {
            return this.contextRoot;
        }

        /**
         * Sets the context root of the <code>Api</code>.
         *
         * @param contextRoot The context root to set.
         */
        public void setContextRoot(String contextRoot) {
            this.contextRoot = contextRoot;
        }

        @Override
        public void handleRequest(ExecutionContext executionContext) {
        }
    }
}
