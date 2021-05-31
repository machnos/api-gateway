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

    private final String id;
    private final String name;

    private Function next;

    protected AbstractFunction(String name) {
        this.id = MACHNOS_FUNCTION_ID_PREFIX + name;
        this.name = MACHNOS_FUNCTION_NAME_PREFIX + name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Function getNext() {
        return this.next;
    }

    @Override
    public void setNext(Function next) {
        this.next = next;
    }


    public void executeNext(ExecutionContext executionContext) {
        if (getNext() != null) {
            getNext().execute(executionContext);
        }
    }
}
