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

package com.machnos.api.gateway.server.domain.api.variables;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Container class for <code>Variable</code>s.
 */
public class Variables {

    /**
     * The set of <code>Variable</code> instances.
     */
    private final Set<Variable<?,?>> variables = new HashSet<>();

    /**
     * Add a <code>Variable</code> to the container.
     *
     * @param variable The <code>Variable</code> to add.
     * @return This <code>Variables</code> instance
     */
    public Variables add(Variable<?,?> variable) {
        this.variables.add(variable);
        return this;
    }

    /**
     * Gets a <code>BooleanVariable</code> by name.
     *
     * @param variableName The name of the <code>BooleanVariable</code>.
     * @return The <code>BooleanVariable</code> with the given name, or <code>null</code> when no such <code>Variable</code> exists.
     */
    public BooleanVariable getBooleanVariable(String variableName) {
        Variable<?, ?> variable = getVariableByName(variableName);
        if (variable instanceof  BooleanVariable) {
            return (BooleanVariable) variable;
        }
        return null;
    }

    /**
     * Gets a <code>NumberVariable</code> by name.
     *
     * @param variableName The name of the <code>NumberVariable</code>.
     * @return The <code>NumberVariable</code> with the given name, or <code>null</code> when no such <code>Variable</code> exists.
     */
    public NumberVariable getNumberVariable(String variableName) {
        Variable<?, ?> variable = getVariableByName(variableName);
        if (variable instanceof  NumberVariable) {
            return (NumberVariable) variable;
        }
        return null;
    }

    /**
     * Gets a <code>StringVariable</code> by name.
     *
     * @param variableName The name of the <code>NumberVariable</code>.
     * @return The <code>StringVariable</code> with the given name, or <code>null</code> when no such <code>Variable</code> exists.
     */
    public StringVariable getStringVariable(String variableName) {
        Variable<?, ?> variable = getVariableByName(variableName);
        if (variable instanceof  StringVariable) {
            return (StringVariable) variable;
        }
        return null;
    }

    /**
     * Gets a variable by name.
     *
     * @param variableName The name of the <code>Variable</code>.
     * @return The <code>Variable</code> with the given name, or <code>null</code> when no such <code>Variable</code> exists.
     */
    private Variable<?, ?> getVariableByName(String variableName) {
        if (variableName == null) {
            return null;
        }
        Optional<Variable<?, ?>> optionalVariable = this.variables.stream().filter(p -> variableName.equals(p.getName())).findAny();
        if (optionalVariable.isEmpty()) {
            return null;
        }
        return optionalVariable.get();
    }
}
