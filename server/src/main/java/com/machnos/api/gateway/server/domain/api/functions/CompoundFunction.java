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

import java.util.ArrayList;
import java.util.List;

public class CompoundFunction extends AbstractFunction {

    private List<Function> functionList = new ArrayList<>();

    public CompoundFunction(String name) {
        super(name);
    }

    @Override
    public Result execute(ExecutionContext executionContext) {
        for (Function function : this.functionList) {
            var result = function.execute(executionContext);
            if (Result.FAILED.equals(result) || Result.STOP_API.equals(result)) {
                return result;
            }
        }
        return Result.SUCCESS;
    }

    public CompoundFunction addFunction(Function function) {
        if (function != null) {
            this.functionList.add(function);
        }
        return this;
    }
}
