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
import com.machnos.api.gateway.server.domain.transport.RequestURL;

/**
 * Mock <code>HttpTransport</code> implementation with setter methods for all properties.
 */
class MockHttpTransport extends MockTransport implements HttpTransport {

    /**
     * Boolean indicating the <code>HttpTransport</code> implements version 0.9 of the http protocol.
     */
    private boolean http09;
    /**
     * Boolean indicating the <code>HttpTransport</code> implements version 1.0 of the http protocol.
     */
    private boolean http10;
    /**
     * Boolean indicating the <code>HttpTransport</code> implements version 1.1 of the http protocol.
     */
    private boolean http11;
    /**
     * Boolean indicating the <code>HttpTransport</code> implements version 1.1 of the http protocol.
     */
    private boolean http20;
    /**
     * The request method.
     */
    private String requestMethod;
    /**
     * The request url
     */
    private RequestURL requestURL;
    /**
     * The response status code.
     */
    private int responseStatusCode;

    @Override
    public boolean isHttp09() {
        return this.http09;
    }

    /**
     * Set the http implementation to version 0.9
     *
     * @param http09 <code>true</code> when the http version should be 0.9, <code>false</code> otherwise.
     */
    public void setHttp09(boolean http09) {
        this.http09 = http09;
    }

    @Override
    public boolean isHttp10() {
        return this.http10;
    }

    /**
     * Set the http implementation to version 1.0
     *
     * @param http10 <code>true</code> when the http version should be 1.0, <code>false</code> otherwise.
     */
    public void setHttp10(boolean http10) {
        this.http10 = http10;
    }

    @Override
    public boolean isHttp11() {
        return this.http11;
    }

    /**
     * Set the http implementation to version 1.1
     *
     * @param http11 <code>true</code> when the http version should be 1.1, <code>false</code> otherwise.
     */
    public void setHttp11(boolean http11) {
        this.http11 = http11;
    }

    @Override
    public boolean isHttp20() {
        return this.http20;
    }

    /**
     * Set the http implementation to version 2.0
     *
     * @param http20 <code>true</code> when the http version should be 2.0, <code>false</code> otherwise.
     */
    public void setHttp20(boolean http20) {
        this.http20 = http20;
    }

    @Override
    public String getRequestMethod() {
        return this.requestMethod;
    }

    /**
     * Sets the request method.
     *
     * @param requestMethod The request method.
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    @Override
    public RequestURL getRequestURL() {
        return this.requestURL;
    }

    /**
     * Sets the <code>RequestURL</code>.
     *
     * @param requestURL The <code>RequestURL</code>.
     */
    public void setRequestURL(RequestURL requestURL) {
        this.requestURL = requestURL;
    }

    @Override
    public int getResponseStatusCode() {
        return this.responseStatusCode;
    }

    @Override
    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
}
