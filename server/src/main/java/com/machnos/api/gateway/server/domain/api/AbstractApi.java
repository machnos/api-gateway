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

package com.machnos.api.gateway.server.domain.api;

import com.machnos.api.gateway.server.domain.api.functions.CompoundFunction;
import com.machnos.api.gateway.server.domain.api.functions.Function;
import com.machnos.api.gateway.server.domain.api.functions.flowlogic.TryFunctions;

/**
 * Abstract superclass for all <code>Api</code> instances.
 */
public abstract class AbstractApi implements Api {

    /**
     * The root <code>Function</code> of the <code>Api</code>.
     */
    private final CompoundFunction rootFunction = new TryFunctions();

    /**
     * Gives the root <code>Function</code> of the api.
     * @return The root code>Function</code> of the api.
     */
    public CompoundFunction getRootFunction() {
        return this.rootFunction;
    }

    /**
     * Adds a child function to the <code>Api</code>.
     * @param function The <code>Function</code> to add to the <code>Api</code>.
     */
    protected void addFunction(Function function) {
        getRootFunction().addFunction(function);
    }

    @Override
    public void handleRequest(ExecutionContext executionContext) {
        this.rootFunction.execute(executionContext);
    }

}
