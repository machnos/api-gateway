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

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Protocols;

/**
 * <code>Transport</code> implementation that is backed by the Undertow <code>HttpServerExchange</code>.
 */
public class UndertowHttpTransport implements HttpTransport {

    /**
     * The alias that is configured with this transport.
     */
    private final String interfaceAlias;

    /**
     * The Undertow <code>HttpServerExchange</code>.
     */
    private final HttpServerExchange httpServerExchange;

    /**
     * The <code>Security</code> implementation for Undertow.
     */
    private final UndertowSecurity security;

    /**
     * The <code>RequestURL</code> implementation for Undertow.
     */
    private final UndertowRequestURL requestUrl;

    /**
     * Constructs a new <code>UndertowHttpTransport</code> instance.
     *
     * @param interfaceAlias The alias of the interface that received the connection.
     * @param httpServerExchange The Undertow <code>HttpServerExchange</code> that is wrapped by this class.
     */
    public UndertowHttpTransport(String interfaceAlias, HttpServerExchange httpServerExchange) {
        this.interfaceAlias = interfaceAlias;
        this.httpServerExchange = httpServerExchange;
        this.security = new UndertowSecurity(this.httpServerExchange);
        this.requestUrl = new UndertowRequestURL(this.httpServerExchange);
    }

    @Override
    public String getInterfaceAlias() {
        return this.interfaceAlias;
    }

    @Override
    public boolean isSecure() {
        return this.httpServerExchange.isSecure();
    }

    @Override
    public UndertowSecurity getSecurity() { return this.security; }

    @Override
    public boolean isHttp09() {
        return this.httpServerExchange.isHttp09();
    }

    @Override
    public boolean isHttp10() {
        return this.httpServerExchange.isHttp10();
    }

    @Override
    public boolean isHttp11() {
        return this.httpServerExchange.isHttp11();
    }

    @Override
    public boolean isHttp20() {
        return Protocols.HTTP_2_0.equals(this.httpServerExchange.getProtocol());
    }

    @Override
    public String getRequestMethod() {
        return this.httpServerExchange.getRequestMethod().toString();
    }

    @Override
    public UndertowRequestURL getRequestURL() {
        return this.requestUrl;
    }

    @Override
    public int getResponseStatusCode() {
        return this.httpServerExchange.getStatusCode();
    }

    @Override
    public void setResponseStatusCode(int statusCode) {
        this.httpServerExchange.setStatusCode(statusCode);
    }
}
