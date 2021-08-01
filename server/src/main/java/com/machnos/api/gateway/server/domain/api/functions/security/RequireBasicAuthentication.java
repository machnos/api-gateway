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

package com.machnos.api.gateway.server.domain.api.functions.security;

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.api.functions.AbstractFunction;
import com.machnos.api.gateway.server.domain.api.functions.Result;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.idm.PasswordCredentials;
import com.machnos.api.gateway.server.domain.message.Headers;
import com.machnos.api.gateway.server.domain.message.HttpMessage;
import com.machnos.api.gateway.server.domain.message.Message;

import java.util.Base64;

/**
 * <code>Function</code> that enforces <a href="https://datatracker.ietf.org/doc/html/rfc7617">Basic Authentication</a>
 * on an <code>Api</code>.
 *
 * The first time this <code>Function</code> is executed a challenge may be sent back. This will result in a {@link Result#isStopped()}
 * <code>Result</code>. This <code>Function</code> has prepared the <code>ResponseMessage</code> with the appropriate
 * challenge in that case.
 *
 * If this <code>Function</code> receives a correct Authorization http header, it will extract the information into an
 * <code>Account</code> and place it on the <code>ExecutionContext</code>. <b>Be aware</b> that the <code>Account</code>
 * that will be created has not been authorized!
 *
 * This function will fail if no/an invalid Authorization header is available on the <code>RequestMessage</code>, or if
 * the {@link Message#isHttp()} returns false on the {@link ExecutionContext#getRequestMessage()}.
 */
public class RequireBasicAuthentication extends AbstractFunction {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Require Basic Authentication";

    /**
     * The <code>Result</code> of this <code>Function</code> in case of a missing authorization header with basic scheme.
     */
    private static final Result RESULT_NO_AUTHORIZATION_HEADER = Result.stop(FUNCTION_NAME, "Authorization header missing", "01");

    /**
     * The <code>Result</code> of this <code>Function</code> in case the request is not http.
     */
    private static final Result RESULT_MESSAGE_NOT_HTTP = Result.fail(FUNCTION_NAME, "Request message is not http", "02");

    /**
     * The <code>Result</code> of this <code>Function</code> in case the request is not http.
     */
    private static final Result RESULT_INVALID_CHALLENGE = Result.fail(FUNCTION_NAME, "Invalid challenge received", "03");

    /**
     * The <code>Result</code> of this <code>Function</code> in case of a missing authorization header with basic scheme.
     */
    private static final Result RESULT_NO_AUTHORIZATION_HEADER_WITH_BASIC_SCHEME = Result.fail(FUNCTION_NAME, "No authorization header with basic scheme", "04");

    /**
     * The default realm name used when no realm has been provided.
     */
    private static final String DEFAULT_REALM_NAME = "Machnos Api Gateway";
    /**
     * The authorization scheme.
     */
    private static final String SCHEME = "Basic";
    /**
     * The Basic Authentication scheme prefix.
     */
    private static final String SCHEME_PREFIX = SCHEME + " ";
    /**
     * The Basic Authentication scheme prefix in lower case.
     */
    private static final String LOWERCASE_SCHEME_PREFIX = SCHEME_PREFIX.toLowerCase();
    /**
     * The value of the realm parameter in the Authorization header.
     */
    private static final String PARAMETER_REALM = "realm";

    /**
     * The realm name.
     */
    private String realmName;

    /**
     * Constructs a new <code>RequireBasicAuthentication</code> instance.
     */
    public RequireBasicAuthentication() {
        super(FUNCTION_NAME);
    }

    /**
     * Gives the realm name.
     * @return The realm name.
     */
    public String getRealmName() {
        return this.realmName;
    }

    /**
     * Sets the realm name.
     *
     * @param realmName The realm name to set.
     * @return This <code>RequireBasicAuthentication</code> instance.
     */
    public RequireBasicAuthentication setRealmName(String realmName) {
        this.realmName = realmName;
        return this;
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        if (!executionContext.getRequestMessage().isHttp()) {
            // Basic authentication is only supported on http requests.
            return RESULT_MESSAGE_NOT_HTTP;
        }
        final var responseMessage = executionContext.getResponseMessage().getHttpMessage();
        final var headers = executionContext.getRequestMessage().getHeaders();
        if (!headers.contains(Headers.HTTP_AUTHORIZATION)) {
            // If no Authorization header is found, we need to send a challenge.
            executionContext.getTransport().getHttpTransport().setResponseStatusCode(HttpMessage.STATUS_CODE_UNAUTHORIZED);
            responseMessage.getHeaders().set(Headers.HTTP_WWW_AUTHENTICATE, getChallenge());
            return RESULT_NO_AUTHORIZATION_HEADER;
        }
        final var authorizationHeaders = executionContext.getRequestMessage().getHeaders().get(Headers.HTTP_AUTHORIZATION);
        for (var authorizationHeader : authorizationHeaders) {
            // Authorization header found, lets check if we can find a value with the basic authentication scheme.
            if (!authorizationHeader.toLowerCase().startsWith(LOWERCASE_SCHEME_PREFIX)) {
                continue;
            }
            // Basic authentication value found. Now let's try to decode it.
            final var encodedChallenge = authorizationHeader.substring(SCHEME_PREFIX.length());
            final var challenge = new String(Base64.getDecoder().decode(encodedChallenge));
            final int ix = challenge.indexOf(":");
            if (ix == -1) {
                return RESULT_INVALID_CHALLENGE;
            }
            // Username + password found! Add it to the account.
            final var username = challenge.substring(0, ix);
            final var credentials = new PasswordCredentials(challenge.substring(ix + 1).toCharArray());
            final var account = new Account(username, credentials);
            executionContext.setAccount(account);
            return Result.succeed();
        }
        return RESULT_NO_AUTHORIZATION_HEADER_WITH_BASIC_SCHEME;
    }

    private String getChallenge() {
        final var realm = getRealmName() != null ? getRealmName() : DEFAULT_REALM_NAME;
        return SCHEME_PREFIX + PARAMETER_REALM + "=\"" + realm + "\"";
    }
 }
