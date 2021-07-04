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

/**
 * <code>RequestURL</code> implementation that is backed by the Undertow <code>HttpServerExchange</code>.
 */
public class UndertowRequestURL implements RequestURL {

    /**
     * The Undertow <code>HttpServerExchange</code>.
     */
    private final HttpServerExchange httpServerExchange;

    /**
     * Constructs a new <code>UndertowRequestURL</code> instance.
     *
     * @param httpServerExchange The Undertow <code>HttpServerExchange</code> that is wrapped by this class.
     */
    public UndertowRequestURL(HttpServerExchange httpServerExchange) {
        this.httpServerExchange = httpServerExchange;
    }

    @Override
    public String getScheme() {
        return this.httpServerExchange.getRequestScheme();
    }

    @Override
    public String getHost() {
        return this.httpServerExchange.getHostName();
    }

    @Override
    public int getPort() {
        return this.httpServerExchange.getHostPort();
    }

    @Override
    public String getPath() {
        return this.httpServerExchange.getRequestPath();
    }

    @Override
    public String getQuery() {
        return this.httpServerExchange.getQueryString();
    }

    @Override
    public String getQueryParameter(String parameterName) {
        final var values = this.httpServerExchange.getQueryParameters().get(parameterName);
        if (values == null) {
            return null;
        }
        return String.join(", ", values);
    }

    @Override
    public String getFragment() {
        return null;
    }

    @Override
    public String toString() {
        var url = this.httpServerExchange.getRequestURL();
        if (this.httpServerExchange.getQueryString() != null) {
            url += "?" + this.httpServerExchange.getQueryString();
        }
        return url;
    }
}