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
 * Mock <code>Transport</code> implementation with setter methods for all properties.
 */
public class MockTransport implements Transport {

    /**
     * The interface alias.
     */
    private String interfaceAlias;
    /**
     * The secure flag.
     */
    private boolean secure;
    /**
     * The <code>Security</code> instance.
     */
    private Security security;
    /**
     * The http flag.
     */
    private boolean http;
    /**
     * The <code>HttpTransport</code> instance.
     */
    private HttpTransport httpTransport;

    @Override
    public String getInterfaceAlias() {
        return this.interfaceAlias;
    }

    /**
     * Sets the interface alias.
     *
     * @param interfaceAlias The interface alias to set.
     */
    public void setInterfaceAlias(String interfaceAlias) {
        this.interfaceAlias = interfaceAlias;
    }

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    /**
     * Sets the secure flag.
     *
     * @param secure The secure flag.
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public Security getSecurity() {
        return this.security;
    }

    /**
     * Sets the <code>Security</code>.
     *
     * @param security The <code>Security</code> to set.
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    @Override
    public boolean isHttp() {
        return this.http;
    }

    /**
     * Sets the http flag.
     *
     * @param http The http flag.
     */
    public void setHttp(boolean http) {
        this.http = http;
    }

    @Override
    public HttpTransport getHttpTransport() {
        return this.httpTransport;
    }

    /**
     * Sets the <code>HttpTransport</code>.
     *
     * @param httpTransport The <code>HttpTransport</code> to set.
     */
    public void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }
}
