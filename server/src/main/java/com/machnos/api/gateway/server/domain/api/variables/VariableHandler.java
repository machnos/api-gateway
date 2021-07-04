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
 * Interface for all classes that can handle variables on a source object.
 *
 * @param <T> The source object that is handled by the implementation.
 */
public interface VariableHandler<T> {

    /**
     * The char that divides object within variable names.
     */
    char OBJECT_DIVIDER = '.';
    /**
     * The length of the object divider.
     */
    int OBJECT_DIVIDER_LENGTH = ("" + OBJECT_DIVIDER).length();

    /**
     * Get a variable from a <code>T</code> instance.
     *
     * @param variable The name of the variable.
     * @param sourceObject The <code>T</code> instance containing the values.
     * @return The value of the variable, or the given <code>T</code> instance when the variable does not exists.
     */
    <I> I getValue(String variable, T sourceObject);
}
