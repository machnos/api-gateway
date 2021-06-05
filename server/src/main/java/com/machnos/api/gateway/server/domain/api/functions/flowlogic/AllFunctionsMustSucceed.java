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
import com.machnos.api.gateway.server.domain.api.functions.CompoundFunction;
import com.machnos.api.gateway.server.domain.api.functions.Function;
import com.machnos.api.gateway.server.domain.api.functions.Result;

public class AllFunctionsMustSucceed extends CompoundFunction {

    public AllFunctionsMustSucceed() {
        super("AllFunctionsMustSucceed");
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        for (Function function : getFunctions()) {
            var result = function.execute(executionContext);
            if (Result.FAILED.equals(result) || Result.STOP_API.equals(result)) {
                return result;
            }
        }
        return Result.SUCCESS;
    }

}
