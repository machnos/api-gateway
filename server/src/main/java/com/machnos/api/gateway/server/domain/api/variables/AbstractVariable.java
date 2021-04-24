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

import java.util.Objects;

/**
 * Abstract superclass for all <code>Variable</code> instances.
 *
 * @param <T> The type of the variable to encapsulate.
 * @param <I> The type of the implementing class.
 */
public abstract class AbstractVariable<T, I extends AbstractVariable<T,I>> implements Variable<T,I> {

    /**
     * The name of the variable.
     */
    private String name;

    /**
     * The value of the variable.
     */
    private T value;

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the variable.
     *
     * @param name The name of the variable.
     * @return The current instance of the <code>AbstractVariable</code>.
     */
    @SuppressWarnings("unchecked")
    public I setName(String name) {
        this.name = name;
        return (I) this;
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public I setValue(T value) {
        this.value = value;
        return (I) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractVariable<?, ?> that = (AbstractVariable<?, ?>) o;
        return Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
