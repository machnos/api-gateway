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

/**
 * Superclass for all <code>Function</code> instances that contain one or more child <code>Function</code>s.
 */
public abstract class CompoundFunction extends AbstractFunction {

    /**
     * The list of child <code>Function</code>s.
     */
    private final List<Function> functions = new ArrayList<>();

    /**
     * Constructs a new <code>CompoundFunction</code> instance.
     * @param name The name of the <code>Function</code>.
     */
    public CompoundFunction(String name) {
        super(name);
    }

    /**
     * Adds a child <code>Function</code>.
     * @param function The <code>Function</code> to add.
     * @return This <code>CompoundFunction</code> instance.
     */
    public CompoundFunction addFunction(Function function) {
        if (function != null) {
            this.functions.add(function);
        }
        return this;
    }

    /**
     * Gives the list with child <code>Function</code>s.
     * @return The list with child <code>Function</code>s.
     */
    public List<Function> getFunctions() {
        return this.functions;
    }

    @Override
    public void preExecute(ExecutionContext executionContext) {
        executionContext.getVariables().startScope();
        super.preExecute(executionContext);
    }

    @Override
    public void postExecute(ExecutionContext executionContext) {
        executionContext.getVariables().endScope();
        super.postExecute(executionContext);
    }
}
