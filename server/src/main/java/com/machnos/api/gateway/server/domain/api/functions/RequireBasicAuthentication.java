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
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.idm.PasswordCredentials;
import com.machnos.api.gateway.server.domain.message.Headers;
import com.machnos.api.gateway.server.domain.message.HttpMessage;

import java.util.Base64;

public class RequireBasicAuthentication extends AbstractFunction {

    private static final String DEFAULT_REALM_NAME = "Machnos Api Gateway";
    private static final String SCHEME = "Basic";
    private static final String SCHEME_PREFIX = SCHEME + " ";
    private static final String LOWERCASE_SCHEME_PREFIX = SCHEME_PREFIX.toLowerCase();
    private static final String PARAMETER_REALM = "realm";

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

    public Result execute(ExecutionContext executionContext) {
        if (!executionContext.getRequestMessage().isHttp()) {
            return Result.FAILED;
        }
        final var responseMessage = executionContext.getResponseMessage().getHttpMessage();
        final var headers = executionContext.getRequestMessage().getHeaders();
        if (!headers.contains(Headers.HTTP_AUTHORIZATION)) {
            executionContext.getTransport().getHttpTransport().setStatusCode(HttpMessage.STATUS_CODE_UNAUTHORIZED);
            responseMessage.getHeaders().set(Headers.HTTP_WWW_AUTHENTICATE, getChallenge());
            return Result.STOP_API;
        }
        final var authorizationHeaders = executionContext.getRequestMessage().getHeaders().get(Headers.HTTP_AUTHORIZATION);
        for (var authorizationHeader : authorizationHeaders) {
            if (!authorizationHeader.toLowerCase().startsWith(LOWERCASE_SCHEME_PREFIX)) {
                continue;
            }
            final var encodedChallenge = authorizationHeader.substring(SCHEME_PREFIX.length());
            final var challenge = new String(Base64.getDecoder().decode(encodedChallenge));
            final int ix = challenge.indexOf(":");
            if (ix == -1) {
                return Result.FAILED;
            }
            final var username = challenge.substring(0, ix);
            final var credentials = new PasswordCredentials(challenge.substring(ix + 1).toCharArray());
            final var account = new Account(username, credentials);
            executionContext.setAccount(account);
            return Result.SUCCESS;
        }
        return Result.FAILED;
    }

    private String getChallenge() {
        final var realm = getRealmName() != null ? getRealmName() : DEFAULT_REALM_NAME;
        return SCHEME_PREFIX + PARAMETER_REALM + "=\"" + realm + "\"";
    }
 }
