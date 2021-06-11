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

package com.machnos.api.gateway.server.domain.transport;

/**
 * Class representing a request URL.
 */
public interface RequestURL {

    /**
     * Gives the scheme of the URL.
     *
     * @return The scheme.
     */
    String getScheme();

    /**
     * Gives the hostname or ip address of the URL.
     *
     * @return The hostname or ip address.
     */
    String getHost();

    /**
     * Gives the port number of the URL.
     *
     * @return The port number.
     */
    int getPort();

    /**
     * Gives the path of the URL.
     *
     * @return The path.
     */
    String getPath();

    /**
     * Gives the query string of the url.
     *
     * @return The query string.
     */
    String getQuery();

    /**
     * Gives the value of a query parameter.
     *
     * @param parameterName The name query of the parameter.
     * @return The value of the query parameter, or <code>null</code> when the parameter does not exists.
     */
    String getQueryParameter(String parameterName);

    /**
     * Gives the fragment of the URL.
     *
     * @return The fragment.
     */
    String getFragment();
}
