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

import com.machnos.api.gateway.server.domain.transport.RequestURL;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock <code>RequestURL</code> implementation with setter methods for all properties.
 */
class MockRequestURL implements RequestURL {

    /**
     * The scheme.
     */
    private String scheme;
    /**
     * The host.
     */
    private String host;
    /**
     * The port.
     */
    private int port;
    /**
     * The path.
     */
    private String path;
    /**
     * The query.
     */
    private String query;
    /**
     * The fragment.
     */
    private String fragment;
    /**
     * The query parameters.
     */
    private final Map<String, String> queryParameters = new HashMap<>();

    @Override
    public String getScheme() {
        return this.scheme;
    }

    /**
     * Set the scheme.
     *
     * @param scheme The scheme.
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    /**
     * Sets the host.
     *
     * @param host The host.
     */
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the port.
     *
     * @param port The port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path.
     *
     * @param path The path.
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    /**
     * Sets the query string.
     * @param query The query string.
     */
    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String getQueryParameter(String parameterName) {
        return this.queryParameters.get(parameterName);
    }

    public void setQueryParameter(String name, String value) {
        this.queryParameters.put(name, value);

    }

    @Override
    public String getFragment() {
        return this.fragment;
    }

    /**
     * Sets the fragment.
     *
     * @param fragment The fragment.
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }
}
