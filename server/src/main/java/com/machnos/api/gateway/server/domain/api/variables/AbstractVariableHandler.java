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

import java.util.regex.Pattern;

/**
 * Abstract superclass for all <code>VariableHandler</code> implementations.
 *
 * @param <T> The source object that is handled by the implementation.
 */
public abstract class AbstractVariableHandler<T> implements VariableHandler<T> {

    /**
     * The char that divides object within variable names.
     */
    protected static final char OBJECT_DIVIDER = '.';
    /**
     * The variable content for retrieving the given object itself instead of a child property.
     */
    protected static final String NO_VARIABLE = "";
    /**
     * The variable suffix to retrieve the size of collections.
     */
    protected static final String SUFFIX_COLLECTION_SIZE = OBJECT_DIVIDER + "size";
    /**
     * The length of the variable suffix to retrieve the size of collections.
     */
    protected static final int SUFFIX_COLLECTION_SIZE_LENGTH = SUFFIX_COLLECTION_SIZE.length();
    /**
     * The regex <code>Pattern</code> to find the nth element of a collection.
     */
    protected static final Pattern COLLECTION_INDEX_PATTERN = Pattern.compile("(.*)\\[(\\d*)]");

    /**
     * Determines the variable name on the child object.
     *
     * This method will strip a leading {@link #OBJECT_DIVIDER} if it is present.
     *
     * @param variable The variable to inspect.
     * @param fromIndex The index to start inspecting.
     *
     * @return The variable name on the child object.
     */
    protected String determineChildObjectVariableName(String variable, int fromIndex) {
        var childVariable = variable.substring(fromIndex);
        if (childVariable.length() > 0 && OBJECT_DIVIDER == childVariable.charAt(0)) {
            return childVariable.substring(1);
        }
        return variable.substring(fromIndex);
    }
}
