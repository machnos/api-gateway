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

/**
 * Mock <code>Api</code> implementation with setter methods for all properties.
 */
class MockApi implements Api {

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

