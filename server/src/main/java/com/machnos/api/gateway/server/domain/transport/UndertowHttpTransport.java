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

public class UndertowHttpTransport implements HttpTransport {

    private final String interfaceAlias;
    private final HttpServerExchange httpServerExchange;

    public UndertowHttpTransport(String interfaceAlias, HttpServerExchange httpServerExchange) {
        this.interfaceAlias = interfaceAlias;
        this.httpServerExchange = httpServerExchange;
    }

    @Override
    public String getInterfaceAlias() {
        return this.interfaceAlias;
    }

    @Override
    public String getRequestMethod() {
        return this.httpServerExchange.getRequestMethod().toString();
    }

    @Override
    public String getRequestPath() {
        return this.httpServerExchange.getRequestPath();
    }
}
