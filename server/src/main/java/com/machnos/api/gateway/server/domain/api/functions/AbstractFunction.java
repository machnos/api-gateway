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

import com.machnos.api.gateway.server.domain.MachnosException;
import com.machnos.api.gateway.server.domain.api.variables.Variable;

/**
 * Abstract superclass for all <code>Function</code> implementations.
 */
public abstract class AbstractFunction implements Function {

    /**
     * Method that checks if a variable is present. If not, a <code>MachnosException</code> will be added to the <code>Result</code> instance.
     *
     * @param variableName The name of the <code>Variable</code>.
     * @param variable The actual <code>Variable</code> that must be present.
     * @param result The <code>Result</code> instance that will be used to add <code>MachnosException</code> to.
     * @return <code>true</code> when an <code>MachnosExceotion</code> is added to the <code>Result</code>, <code>false</code> otherwise.
     */
    protected boolean requireVariable(String variableName, Variable<?,?> variable, Result result) {
        var hasError = false;
        if (variable == null) {
            result.addException(new MachnosException(MachnosException.MISSING_VARIABLE, variableName));
            hasError = true;
        }
        return hasError;
    }

    /**
     * Method that checks if a variable is present and has a value. If not, a <code>MachnosException</code> will be added to the <code>Result</code> instance.
     *
     * @param variableName The name of the <code>Variable</code>.
     * @param variable The actual <code>Variable</code> that must be present and must contain a value.
     * @param result The <code>Result</code> instance that will be used to add <code>MachnosException</code> to.
     * @return <code>true</code> when an <code>MachnosExceotion</code> is added to the <code>Result</code>, <code>false</code> otherwise.
     */
    protected boolean requireVariableWithValue(String variableName, Variable<?,?> variable, Result result) {
        var hasError = false;
        if (variable == null) {
            result.addException(new MachnosException(MachnosException.MISSING_VARIABLE, variableName));
            hasError = true;
        }
        if (!variable.hasValue()) {
            result.addException(new MachnosException(MachnosException.MISSING_VALUE, variableName));
            hasError = true;
        }
        return hasError;
    }

}
