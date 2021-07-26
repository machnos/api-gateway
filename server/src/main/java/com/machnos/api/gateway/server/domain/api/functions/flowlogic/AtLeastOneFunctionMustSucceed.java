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

/**
 * <code>CompoundFunction</code> that succeeds when all child <code>Function</code>s are successfully executed.
 *
 * This <code>CompoundFunction</code> stops executing child <code>Function</code>s as soon as a child <code>Function</code>
 * succeeds. The order in which the child <code>Function</code>s are executed is the order in which the are added to
 * the <code>CompoundFunction</code>.
 */
public class AtLeastOneFunctionMustSucceed extends CompoundFunction {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "At Least One Functions Must Succeed";

    /**
     * The <code>Result</code> of this <code>Function</code> in case of a child failure.
     */
    private static final Result RESULT_NO_CHILD_FUNCTION_SUCCEEDED = Result.fail(FUNCTION_NAME + " - No child function succeeded - 01");

    /**
     * Constructs a new <code>AtLeastOneFunctionMustSucceed</code> instance.
     */
    public AtLeastOneFunctionMustSucceed() {
        super(FUNCTION_NAME);
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        for (Function function : getFunctions()) {
            var result = function.execute(executionContext);
            if (result.isSucceeded()) {
                return result;
            }
            if (result.isStopApi()) {
                return result;
            }
        }
        return RESULT_NO_CHILD_FUNCTION_SUCCEEDED;
    }

}
