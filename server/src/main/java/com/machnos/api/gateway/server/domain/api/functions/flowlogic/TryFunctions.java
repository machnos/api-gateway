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

import java.util.ArrayList;
import java.util.List;

/**
 * A <code>CompoundFunction</code> that tries to execute a list of child <code>Function</code>s and is able to recover
 * from failed child executions.
 *
 * If any of the child <code>Function</code>s fails the error functions are executed. If any of the error
 * <code>Functions</code>s fails the <code>TryFunctions</code> will also fail.
 */
public class TryFunctions extends CompoundFunction<TryFunctions> {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Try Functions";

    /**
     * The list of child error <code>Function</code>s.
     */
    private final List<Function> errorFunctions = new ArrayList<>();

    /**
     * Constructs a new <code>CompoundFunction</code> instance.
     */
    public TryFunctions() {
        super(FUNCTION_NAME);
    }

    /**
     * Adds a child error <code>Function</code>.
     *
     * @param function The <code>Function</code> to add.
     * @return This <code>TryFunctions</code> instance.
     */
    public TryFunctions addErrorFunction(Function function) {
        if (function != null) {
            this.errorFunctions.add(function);
        }
        return this;
    }

    /**
     * Gives the list with child error <code>Function</code>s.
     *
     * @return The list with child error <code>Function</code>s.
     */
    public List<Function> getErrorFunctions() {
        return this.errorFunctions;
    }

    @Override
    protected Result doExecute(ExecutionContext executionContext) {
        Result childResult = null;
        for (Function function : getFunctions()) {
            childResult = function.execute(executionContext);
            if (childResult.isFailed() || childResult.isStopped()) {
                break;
            }
        }
        if (childResult != null && childResult.isStopped()) {
            return childResult;
        } else if (childResult != null && childResult.isFailed()) {
            return executeError(executionContext);
        }
        return Result.succeed();
    }

    /**
     * Execute the error branch of this <code>Function</code>.
     *
     * @param executionContext The <code>ExecutionContext</code> that is used to execute the <code>Function</code>s.
     * @return The <code>Result</code> of the execution.
     */
    private Result executeError(ExecutionContext executionContext) {
        for (Function function : getErrorFunctions()) {
            var childResult = function.execute(executionContext);
            if (childResult.isFailed()) {
                return Result.fail(FUNCTION_NAME + " - Error function failed - 01", childResult);
            } else if (childResult.isStopped()) {
                return childResult;
            }
        }
        return Result.succeed();
    }
}
