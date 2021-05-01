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

import io.undertow.io.IoCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.URLResource;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class ClasspathResourceHttpHandler implements HttpHandler {

    private final ClassLoader classLoader;
    private final String rootPackage;

    public ClasspathResourceHttpHandler(String rootPackage) {
        this(rootPackage, ClasspathResourceHttpHandler.class.getClassLoader());
    }

    public ClasspathResourceHttpHandler(String rootPackage, ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.rootPackage = rootPackage;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        final var requestPath = this.rootPackage + exchange.getRequestPath().toLowerCase();
        final var resource = this.classLoader.getResource(requestPath);
        if (resource == null) {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
        } else {
            final var urlResource = new URLResource(resource, exchange.getRequestPath());
            setContentType(exchange, requestPath);
            urlResource.serve(exchange.getResponseSender(), exchange, IoCallback.END_EXCHANGE);
        }
    }

    private void setContentType(HttpServerExchange exchange, String requestPath) {
        if (requestPath.endsWith(".html")) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html; charset=utf-8");
        } else if (requestPath.endsWith(".js")) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/javascript; charset=utf-8");
        } else if (requestPath.endsWith(".css")) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/css; charset=utf-8");
        }
    }
}
