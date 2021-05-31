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

package com.machnos.api.gateway.server.domain.message;

import com.machnos.api.gateway.server.domain.MachnosException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;

import java.util.ArrayList;
import java.util.List;

public class UndertowHeaders implements Headers {

    private final HttpServerExchange httpServerExchange;
    private final UndertowHttpMessage.Type type;

    public UndertowHeaders(HttpServerExchange httpServerExchange, UndertowHttpMessage.Type type) {
        this.httpServerExchange = httpServerExchange;
        this.type = type;
    }

    private HeaderMap getHeaderMap() {
        return this.type.isRequest() ? this.httpServerExchange.getRequestHeaders() : this.httpServerExchange.getResponseHeaders();
    }

    @Override
    public boolean contains(String headerName) {
        return getHeaderMap().contains(headerName);
    }

    @Override
    public String getFirst(String headerName) {
        return getHeaderMap().getFirst(headerName);
    }

    @Override
    public List<String> get(String headerName) {
        final var headerValues = getHeaderMap().get(headerName);
        if (headerValues == null) {
            return null;
        }
        return new ArrayList<>(headerValues);
    }

    @Override
    public void set(String headerName, String headerValue) {
        if (this.type.isRequest()) {
            throw new MachnosException(MachnosException.OBJECT_IS_IMMUTABLE, "headers");
        }
        HttpString httpString = io.undertow.util.Headers.fromCache(headerName);
        if (httpString == null) {
            httpString = new HttpString(headerName);
        }
        getHeaderMap().put(httpString, headerValue);
    }

}
