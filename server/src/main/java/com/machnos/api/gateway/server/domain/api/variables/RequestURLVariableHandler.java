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

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>RequestURL</code> instance.
 */
public class RequestURLVariableHandler extends AbstractVariableHandler<RequestURL> {

    /**
     * The prefix for a specific query parameter.
     */
    public static final String PREFIX_QUERY_PARAMETER = "query" + AbstractVariableHandler.OBJECT_DIVIDER;
    /**
     * The length of the prefix for a specific query parameter.
     */
    public static final int PREFIX_QUERY_PARAMETER_LENGTH = PREFIX_QUERY_PARAMETER.length();
    /**
     * The scheme variable.
     */
    public static final String SCHEME = "scheme";
    /**
     * The host variable.
     */
    public static final String HOST = "host";
    /**
     * The port variable.
     */
    public static final String PORT = "port";
    /**
     * The path variable.
     */
    public static final String PATH = "path";
    /**
     * The fragment variable.
     */
    public static final String FRAGMENT = "fragment";
    /**
     * The query variable.
     */
    public static final String QUERY = "query";

    @Override
    @SuppressWarnings("unchecked")
    public <I> I getValue(String variable, RequestURL requestURL) {
        if (variable == null || requestURL == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return (I) requestURL;
        } else if (SCHEME.equals(variable)) {
            return (I) requestURL.getScheme();
        } else if (HOST.equals(variable)) {
            return (I) requestURL.getHost();
        } else if (PORT.equals(variable)) {
            return (I) Integer.valueOf(requestURL.getPort());
        } else if (PATH.equals(variable)) {
            return (I) requestURL.getPath();
        } else if (FRAGMENT.equals(variable)) {
            return (I) requestURL.getFragment();
        } else if (QUERY.equals(variable)) {
            return (I) requestURL.getQuery();
        } else if (variable.startsWith(PREFIX_QUERY_PARAMETER)) {
            return (I) requestURL.getQueryParameter(determineChildObjectVariableName(variable, PREFIX_QUERY_PARAMETER_LENGTH));
        }
        return null;
    }
}
