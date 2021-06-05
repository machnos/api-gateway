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
import com.machnos.api.gateway.server.domain.api.functions.flowlogic.AllFunctionsMustSucceed;

public abstract class AbstractApi implements Api {

    private final CompoundFunction rootFunction = new AllFunctionsMustSucceed();

    public CompoundFunction getRootFunction() {
        return this.rootFunction;
    }

    protected void addFunction(Function function) {
        getRootFunction().addFunction(function);
    }

    @Override
    public void handleRequest(ExecutionContext executionContext) {
        this.rootFunction.execute(executionContext);
    }

}
