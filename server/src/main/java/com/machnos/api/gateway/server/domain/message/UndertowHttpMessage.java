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

/**
 * <code>HttpMessage</code> implementation backed by the Undertow <code>HttpServerExchange</code>.
 */
public class UndertowHttpMessage implements HttpMessage {

    /**
     * The message body.
     */
    private String body;

    /**
     * The Undertow <code>HttpServerExchange</code>.
     */
    private final HttpServerExchange httpServerExchange;
    /**
     * The http message type.
     */
    private final Type type;
    /**
     * The <code>UndertowHeaders</code>.
     */
    private final UndertowHeaders headers;

    /**
     * Constructs a new <code>UndertowHttpMessage</code> instance.
     *
     * @param httpServerExchange The <code>HttpServerExchange</code> that holds all values encapsulated by this class.
     * @param type The http message type.
     */
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

    @Override
    public String getBody() {
        if (this.type.isRequest() && this.body == null) {
            this.httpServerExchange.getRequestReceiver().receiveFullString(
                    (exchange, message) -> this.body = message,
                    ((exchange, e) -> this.body = "")
            );
        }
        return this.body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

}
