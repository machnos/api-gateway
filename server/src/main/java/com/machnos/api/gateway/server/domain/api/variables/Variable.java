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
 * Class representing a variable that is in essence a key-value pair.
 *
 * @param <T> The type of the variable to encapsulate.
 * @param <I> The type of the implementing class.
 */
public interface Variable<T, I extends Variable<T,I>> {

    /**
     * Gives the name of the <code>Variable</code>.
     *
     * @return The name of the <code>Variable</code>.
     */
    String getName();

    /**
     * Gives the current value of the <code>Variable</code>.
     *
     * @return The current value of the <code>Variable</code>.
     */
    T getValue();

    /**
     * Sets the value of the <code>Variable</code>.
     *
     * @param t The value
     * @return The current instance of the <code>Variable</code>.
     */
    I setValue(T t);

    /**
     * Determines whether or not this <code>Variable</code> has a value.
     *
     * @return <code>true</code> when this <code>Variable</code> has a value, <code>false</code> otherwise.
     */
    default boolean hasValue() {
        return getValue() != null;
    }
}
