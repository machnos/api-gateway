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

/**
 * Abstract superclass for all <code>Function</code> implementations.
 */
public abstract class AbstractFunction implements Function {

    /**
     * The unique id of the <code>Function</code>.
     */
    private final String id;

    /**
     * The name of the <code>Function</code>.
     */
    private final String name;

    /**
     * Constructs a new <code>AbstractFunction</code> instance.
     *
     * @param name The name of the <code>Function</code>.
     */
    protected AbstractFunction(String name) {
        this.id = MACHNOS_FUNCTION_ID_PREFIX + name;
        this.name = MACHNOS_FUNCTION_NAME_PREFIX + name;
    }

    @Override
    public final String getId() {
        return this.id;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final Result execute(ExecutionContext executionContext) {
        preExecute(executionContext);
        var result = doExecute(executionContext);
        postExecute(executionContext);
        return result;
    }

    /**
     * Method called before the {@link #doExecute(ExecutionContext)} method is called. This gives implementing classes
     * the option to do some initialization work.
     *
     * @param executionContext The <code>ExecutionContext</code> that is used to execute the <code>Function</code>.
     */
    public void preExecute(ExecutionContext executionContext) {}

    /**
     * Method called after the {@link #doExecute(ExecutionContext)} method is called. This gives implementing classes
     * the option to do some cleanup work.
     *
     * @param executionContext The <code>ExecutionContext</code> that is used to execute the <code>Function</code>.
     */
    public void postExecute(ExecutionContext executionContext) {}

    /**
     * Execute the business logic of the implementing class.
     *
     * @param executionContext The <code>ExecutionContext</code> that is used to execute the <code>Function</code>.
     * @return The <code>Result</code> of the execution.
     */
    protected abstract Result doExecute(ExecutionContext executionContext);
}
