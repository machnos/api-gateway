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

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that is capable of managing variables in a certain scope.
 */
public class Variables {

    /**
     * The <code>ArrayDeque</code> that holds the variables at a certain scope.
     */
    private final ArrayDeque<Map<String, Object>> variableScopes = new ArrayDeque<>();

    /**
     * Start a new level. This means that all variables added after this call will be alive as long as the
     * {@link #endScope()} is called. This makes it possible for <code>CompoundFunction</code>s to allow variables that
     * only live during the execution of that <code>CompoundFunction</code>.
     */
    public void startScope() {
        this.variableScopes.offerFirst(new HashMap<>());
    }

    /**
     * End the current scope. All variables add in the current scope will be discarded.
     */
    public void endScope() {
        this.variableScopes.pollFirst();
    }

    /**
     * Set a variable. If the variable already exists in a certain scope, that value will be updated. If the variable
     * not yet exists, it will be created in the current scope.
     *
     * @param variableName The name of the variable.
     * @param variableValue The value of the variable.
     */
    public void setVariable(String variableName, Object variableValue) {
        if (variableName == null) {
            return;
        }
        final var variable = variableName.toLowerCase();
        for (Map<String, Object> variables : this.variableScopes) {
            if (variables.containsKey(variable)) {
                variables.put(variable, variableValue);
                return;
            }
        }
        this.variableScopes.peekFirst().put(variable, variableValue);
    }

    /**
     * Get a variable.
     *
     * @param variableName The name of the variable.
     * @return The value, or <code>null</code> whe no variable with the given name exists.
     */
    public Object getVariable(String variableName) {
        if (variableName == null) {
            return null;
        }
        final var variable = variableName.toLowerCase();
        for (Map<String, Object> variables : this.variableScopes) {
            if (variables.containsKey(variable)) {
                return variables.get(variable);
            }
        }
        return null;
    }
}
