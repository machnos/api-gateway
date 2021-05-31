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

package com.machnos.api.gateway.server.domain.api.functions;

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.message.Headers;
import com.machnos.api.gateway.server.domain.message.HttpMessage;

public class RequireBasicAuthentication extends AbstractFunction {

    private String realmName;

    public RequireBasicAuthentication() {
        super("RequireBasicAuthentication");
    }

    public String getRealmName() {
        return this.realmName;
    }

    public RequireBasicAuthentication setRealmName(String realmName) {
        this.realmName = realmName;
        return this;
    }

    public void execute(ExecutionContext executionContext) {
        if (!executionContext.getResponseMessage().isHttp()) {
            return;
        }
        final var responseMessage = executionContext.getResponseMessage().getHttpMessage();
        final var headers = executionContext.getRequestMessage().getHeaders();
        if (!headers.contains(Headers.HTTP_AUTHORIZATION)) {
            responseMessage.setStatusCode(HttpMessage.STATUS_CODE_UNAUTHORIZED);
            responseMessage.getHeaders().set(Headers.HTTP_WWW_AUTHENTICATE, getChallenge());
        } else {
            executeNext(executionContext);
        }
    }

    private String getChallenge() {
        return getRealmName() != null ? "Basic realm=\"" + getRealmName() + "\"" : "Basic";
    }
 }
