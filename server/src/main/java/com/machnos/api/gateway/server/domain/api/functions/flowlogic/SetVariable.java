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
 * A <code>Function</code> that can set a variable to the <code>ExecutionContext</code>.
 */
public class SetVariable extends AbstractFunction  {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Set Variable";

    /**
     * The <code>Result</code> of this <code>Function</code> in case of a child failure.
     */
    private static final Result RESULT_NAME_NOT_SET = Result.fail(FUNCTION_NAME + " - Variable name not set - 01");

    /**
     * The name of the variable.
     */
    private String variableName;

    /**
     * The value of the variable.
     */
    private Object variableValue;

    /**
     * Constructs a new <code>SetVariable</code> instance.
     */
    public SetVariable() {
        super(FUNCTION_NAME);
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        if (getVariableName() == null) {
            return RESULT_NAME_NOT_SET;
        }
        executionContext.getVariables().setVariable(
                executionContext.parseVariableAsString(getVariableName()),
                getVariableValue() instanceof String ? executionContext.parseVariableAsString((String) getVariableValue()) : getVariableValue());
        return Result.succeed();
    }

    public String getVariableName() {
        return this.variableName;
    }

    public SetVariable setVariableName(String name) {
        this.variableName = name;
        return this;
    }

    public Object getVariableValue() {
        return this.variableValue;
    }

    public SetVariable setVariableValue(Object variableValue) {
        this.variableValue = variableValue;
        return this;
    }
}
