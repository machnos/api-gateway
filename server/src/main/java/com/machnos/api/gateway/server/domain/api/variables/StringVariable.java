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

/**
 * <code>Variable</code> implementation that encapsulates a String value.
 */
public class StringVariable extends AbstractVariable<String, StringVariable> {

    private static final Integer ZERO = 0;

    /**
     * Determines if the current value starts with a given prefix.
     *
     * @param prefix The prefix to test.
     *
     * @return <code>true</code> if the current value starts with the prefix, <code>false</code> otherwise.
     */
    public boolean startsWith(String prefix) {
        return hasValue() && getValue().startsWith(prefix);
    }

    /**
     * Determines if the current value ends with a given suffix.
     *
     * @param suffix The suffix to test.
     *
     * @return <code>true</code> if the current value ends with the suffix, <code>false</code> otherwise.
     */
    public boolean endsWith(String suffix) {
        return hasValue() && getValue().endsWith(suffix);
    }

    /**
     * Gives the length of the current value.
     *
     * @return The length of the value, or a zero integer when the current value is <code>null</code>.
     */
    public Integer getLength() {
        return hasValue() ? getValue().length() : ZERO;
    }

}
