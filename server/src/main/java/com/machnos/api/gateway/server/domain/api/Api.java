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

package com.machnos.api.gateway.server.domain.api;

/**
 * Interface that represents an <code>Api</code>.
 */
public interface Api {

    /**
     * Gives the name of the <code>Api</code>.
     *
     * @return The name of the <code>Api</code>.
     */
    String getName();

    /**
     * Gives the context root of the <code>Api</code>.
     *
     * This value is only available for <code>Api</code>'s that are available via http.
     *
     * @return The context root of the <code>Api</code>.
     */
    String getContextRoot();

    /**
     * Handle a request to this <code>Api</code>.
     * @param executionContext The <code>ExecutionContext</code> in which the <code>Api</code> is executed.
     */
    void handleRequest(ExecutionContext executionContext);
}
