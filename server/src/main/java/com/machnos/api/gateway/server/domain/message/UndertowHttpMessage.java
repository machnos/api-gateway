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

import io.undertow.server.HttpServerExchange;

public class UndertowHttpMessage implements HttpMessage {

    public enum Type {
        REQUEST, RESPONSE;

        public boolean isRequest() {
            return Type.REQUEST.equals(this);
        }

        public boolean isResponse() {
            return Type.RESPONSE.equals(this);
        }
    }

    private final HttpServerExchange httpServerExchange;
    private final Type type;
    private final UndertowHeaders headers;

    public UndertowHttpMessage(HttpServerExchange httpServerExchange, Type type) {
        super();
        this.httpServerExchange = httpServerExchange;
        this.type = type;
        this.headers = new UndertowHeaders(httpServerExchange, type);
    }

    @Override
    public Headers getHeaders() {
        return this.headers;
    }

}
