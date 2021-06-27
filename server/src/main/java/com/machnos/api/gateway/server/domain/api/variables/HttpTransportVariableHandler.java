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

import com.machnos.api.gateway.server.domain.transport.HttpTransport;

/**
 * <code>VariableHandler</code> implementation that can handle values of an <code>HttpTransport</code> instance.
 */
public class HttpTransportVariableHandler extends AbstractVariableHandler<HttpTransport> {

    /**
     * The prefix for the request url object.
     */
    public static final String PREFIX_REQUEST_URL = "request.url";
    /**
     * The length of the prefix for the request url object.
     */
    public static final int PREFIX_REQUEST_URL_LENGTH = PREFIX_REQUEST_URL.length();
    /**
     * The request method variable.
     */
    public static final String REQUEST_METHOD = "request.method";
    /**
     * The response status code variable.
     */
    public static final String RESPONSE_STATUS_CODE = "response.status_code";
    /**
     * The is http 0.9 variable.
     */
    public static final String IS_HTTP_09 = "ishttp09";
    /**
     * The is http 1.0 variable.
     */
    public static final String IS_HTTP_10 = "ishttp10";
    /**
     * The is http 1.1 variable.
     */
    public static final String IS_HTTP_11 = "ishttp11";
    /**
     * The is http 2.0 variable.
     */
    public static final String IS_HTTP_20 = "ishttp20";

    /**
     * The <code>VariableHandler</code> that can handle values for the <code>RequestURL</code> objects.
     */
    private final RequestURLVariableHandler requestURLVariableHandler = new RequestURLVariableHandler();

    @Override
    @SuppressWarnings("unchecked")
    public <I> I getValue(String variable, HttpTransport httpTransport) {
        if (variable == null || httpTransport == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return (I) httpTransport;
        } else if (REQUEST_METHOD.equals(variable)) {
            return (I) httpTransport.getRequestMethod();
        } else if (variable.startsWith(PREFIX_REQUEST_URL)) {
            return this.requestURLVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_REQUEST_URL_LENGTH), httpTransport.getRequestURL());
        } else if (variable.equals(RESPONSE_STATUS_CODE)) {
            return (I) Integer.valueOf(httpTransport.getResponseStatusCode());
        } else if (IS_HTTP_09.equals(variable)) {
            return (I) Boolean.valueOf(httpTransport.isHttp09());
        } else if (IS_HTTP_10.equals(variable)) {
            return (I) Boolean.valueOf(httpTransport.isHttp10());
        } else if (IS_HTTP_11.equals(variable)) {
            return (I) Boolean.valueOf(httpTransport.isHttp11());
        } else if (IS_HTTP_20.equals(variable)) {
            return (I) Boolean.valueOf(httpTransport.isHttp20());
        }
        return null;
    }
}
