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
import com.machnos.api.gateway.server.domain.api.functions.AbstractFunction;
import com.machnos.api.gateway.server.domain.api.functions.Result;

/**
 * A <code>Function</code> that does nothing more that returning a stop result.
 */
public class Stop extends AbstractFunction {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Stop";

    /**
     * The <code>Result</code> of this <code>Function</code>.
     */
    private static final Result RESULT = Result.stop(FUNCTION_NAME + " - Stop function executed - 01");

    /**
     * Constructs a new <code>Stop</code> instance.
     */
    public Stop() {
        super(FUNCTION_NAME);
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        return RESULT;
    }
}
