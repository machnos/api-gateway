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

package com.machnos.api.gateway.server.http;

import com.machnos.api.gateway.server.domain.api.DummyWebApi;
import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.message.UndertowHttpMessage;
import com.machnos.api.gateway.server.domain.transport.UndertowHttpTransport;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class ApiHttpHandler implements HttpHandler {

    private final String interfaceAlias;

    public ApiHttpHandler(String interfaceAlias) {
        this.interfaceAlias = interfaceAlias;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }
        final var transport = new UndertowHttpTransport(exchange);
        final var requestMessage = new UndertowHttpMessage(exchange, UndertowHttpMessage.Type.REQUEST);
        final var responseMessage = new UndertowHttpMessage(exchange, UndertowHttpMessage.Type.RESPONSE);
        final var executionContext = new ExecutionContext(transport, requestMessage, responseMessage);


        // Step 1. Find api based on path.
        String requestPath = transport.getRequestPath();
        if ("/".equals(requestPath)) {

            new DummyWebApi().handleRequest(executionContext);
        }

        if (responseMessage.getBody() != null) {
            exchange.getResponseSender().send(responseMessage.getBody());
        }
        exchange.endExchange();
    }
}
