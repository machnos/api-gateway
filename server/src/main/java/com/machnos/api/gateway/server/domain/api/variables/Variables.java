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

public class Variables {

    private final ArrayDeque<Map<String, Object>> variableLevels = new ArrayDeque<>();

    public void startLevel() {
        this.variableLevels.offerFirst(new HashMap<>());
    }

    public void endLevel() {
        this.variableLevels.pollFirst();
    }

    public void setVariable(String variableName, Object variableValue) {
        for (Map<String, Object> variables : this.variableLevels) {
            if (variables.containsKey(variableName)) {
                variables.put(variableName, variableValue);
                return;
            }
        }
        this.variableLevels.peekFirst().put(variableName, variableValue);
    }

    public Object getVariable(String variableName) {
        for (Map<String, Object> variables : this.variableLevels) {
            if (variables.containsKey(variableName)) {
                return variables.get(variableName);
            }
        }
        return null;
    }
}
