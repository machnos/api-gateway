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
import com.machnos.api.gateway.server.domain.api.variables.Variables;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a result of the execution of a <code>Function</code>.
 */
public class Result {

    /**
     * The <code>Variables</code> that are the result of the execution of a <code>Function</code>.
     */
    private final Variables outputVariables = new Variables();

    /**
     * A list with <code>MachnosException</code>s that are the result of the execution of a <code>Function</code>.
     */
    private final List<MachnosException> exceptions = new ArrayList<>();

    /**
     * Add a <code>MachtnosException</code> to the result.
     *
     * @param exception The <code>Exception</code> to add to this <code>Result</code>.
     * @return This <code>Result</code> instance.
     */
    public Result addException(MachnosException exception) {
        this.exceptions.add(exception);
        return this;
    }

    /**
     * Boolean indicating this <code>Result</code> has <code>MachnosExecptions</code>s. The execution of a <code>Function</code> is considered successful when the <code>Result</code> has no <code>MachnosExecptions</code>s.
     * @return <code>true</code> when there are <code>MachnosExecptions</code>s present in the <code>Result</code>, <code>false</code> otherwise.
     */
    public boolean hasExceptions() {
        return this.exceptions.size() > 0;
    }

    /**
     * Add a <code>Variable</code> to the list of output <code>Variable</code>s.
     *
     * @param variable The <code>Variable</code> to add.
     * @return This <code>Result</code> instance.
     */
    public Result addOutputVariable(Variable<?,?> variable) {
        this.outputVariables.add(variable);
        return this;
    }

    /**
     * Gives the output <code>Variable</code>s.
     *
     * @return The output <code>Variable</code>s.
     */
    public Variables getOutputVariables() {
        return this.outputVariables;
    }
}
