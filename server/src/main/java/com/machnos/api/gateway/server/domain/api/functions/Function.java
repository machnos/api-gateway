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

import com.machnos.api.gateway.server.domain.api.variables.Variables;

import java.util.Map;

/**
 * Base class for all elements that form a policy. The logic of an api is determined by the set of policies that is
 * configured for the specific api's.
 *
 * A function can have optional input and output variables, and must have an outcome.
 */
public interface Function {

    /**
     * The prefix for all function ids that are supplied with Machnos out of the box.
     */
    String MACHNOS_FUNCTION_ID_PREFIX = "MachnosFunctionId_";

    /**
     * The prefix for all function names that are supplied with Machnos out of the box.
     */
    String MACHNOS_FUNCTION_NAME_PREFIX = "MachnosFunctionName_";


    /**
     * Gives the id of the <code>Function</code>. The id should be globally unique.
     *
     * @return The unique id of the <code>Function</code>.
     */
    String getId();

    /**
     * Gives the name of the <code>Function</code>.
     *
     * @return The name of the <code>Function</code>.
     */
    String getName();

    /**
     * Execute the <code>Function</code>.
     *
     * @param inputVariables The input <code>Variables</code>.
     * @param functionConfiguration The configuration settings of the <code>Function</code>.
     * @return The <code>Result</code> of the execution.
     */
    Result execute(final Variables inputVariables, Map<String, String> functionConfiguration);
}
