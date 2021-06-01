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

package com.machnos.api.gateway.server.domain.api.variables;

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.Transport;

public class VariableParser {

    private static final String VARIABLE_PREFIX = "${";
    private static final int VARIABLE_PREFIX_LENGTH = VARIABLE_PREFIX.length();
    private static final String VARIABLE_SUFFIX = "}";

    private static final String ACCOUNT_VARIABLE_PREFIX = "account.";
    private static final int ACCOUNT_VARIABLE_PREFIX_LENGTH = ACCOUNT_VARIABLE_PREFIX.length();
    private static final String TRANSPORT_VARIABLE_PREFIX = "transport.";
    private static final int TRANSPORT_VARIABLE_PREFIX_LENGTH = TRANSPORT_VARIABLE_PREFIX.length();
    private static final String REQUEST_VARIABLE_PREFIX = "request.";
    private static final int REQUEST_VARIABLE_PREFIX_LENGTH = REQUEST_VARIABLE_PREFIX.length();
    private static final String RESPONSE_VARIABLE_PREFIX = "response.";
    private static final int RESPONSE_VARIABLE_PREFIX_LENGTH = RESPONSE_VARIABLE_PREFIX.length();
    private static final String HEADER_VARIABLE_PREFIX = "header.";
    private static final int HEADER_VARIABLE_PREFIX_LENGTH = HEADER_VARIABLE_PREFIX.length();
    private static final String HEADERS_VARIABLE_PREFIX = "headers.";
    private static final int HEADERS_VARIABLE_PREFIX_LENGTH = HEADERS_VARIABLE_PREFIX.length();
    private static final String COLLECTION_VARIABLE_SUFFIX_FIRST = ".first";
    private static final int COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH = COLLECTION_VARIABLE_SUFFIX_FIRST.length();


    public String parse(String input, ExecutionContext executionContext) {
        if (input == null) {
            return null;
        }
        var result = input;
        while (true) {
            final var startIx = result.lastIndexOf(VARIABLE_PREFIX);
            if (startIx == -1) {
                return result;
            }
            final var endIx = result.indexOf(VARIABLE_SUFFIX, startIx + VARIABLE_PREFIX_LENGTH);
            if (endIx == -1) {
                return result;
            }
            final var variableName = result.substring(startIx + VARIABLE_PREFIX_LENGTH, endIx);
            final var variableValue = getValue(variableName, executionContext);
            result = result.substring(0, startIx) + (variableValue != null ? variableValue.toString() : "") + result.substring(endIx + 1);
        }
    }

    private Object getValue(String variableName, ExecutionContext executionContext) {
        if (variableName == null) {
            return null;
        }
        final var variable = variableName.toLowerCase();
        if (variable.startsWith(REQUEST_VARIABLE_PREFIX)) {
            return executionContext.getRequestMessage() != null ? getMessageValue(variable.substring(REQUEST_VARIABLE_PREFIX_LENGTH), executionContext.getRequestMessage()) : null;
        } else if (variable.startsWith(RESPONSE_VARIABLE_PREFIX)) {
            return executionContext.getResponseMessage() != null ? getMessageValue(variable.substring(RESPONSE_VARIABLE_PREFIX_LENGTH), executionContext.getResponseMessage()) : null;
        } else if (variable.startsWith(ACCOUNT_VARIABLE_PREFIX)) {
            return executionContext.getAccount() != null ? getAccountValue(variable.substring(ACCOUNT_VARIABLE_PREFIX_LENGTH), executionContext.getAccount()) : null;
        } else if (variable.startsWith(TRANSPORT_VARIABLE_PREFIX)) {
            return executionContext.getTransport() != null ? getTransportValue(variable.substring(TRANSPORT_VARIABLE_PREFIX_LENGTH), executionContext.getTransport()) : null;
        }
        return null;
    }

    private Object getMessageValue(String variable, Message message) {
        if ("body".equals(variable)) {
            return message.getBody();
        } else if (variable.startsWith(HEADER_VARIABLE_PREFIX)) {
            if (variable.endsWith(COLLECTION_VARIABLE_SUFFIX_FIRST)) {
                var headerName = variable.substring(HEADER_VARIABLE_PREFIX_LENGTH, variable.length() - COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH);
                var values = message.getHeaders().get(headerName);
                if (values == null || values.size() == 0) {
                    return null;
                }
                return values.get(0);
            }
            return message.getHeaders().get(variable.substring(HEADER_VARIABLE_PREFIX_LENGTH));
        } else if ("ishttp".equals(variable)) {
            return message.isHttp();
        }
        return null;
    }

    private Object getAccountValue(String variable, Account account) {
        if ("username".equals(variable)) {
            return account.getUsername();
        }
        return null;
    }

    private Object getTransportValue(String variable, Transport transport) {
        if ("http.request.method".equals(variable)) {
            return transport.isHttp() ? transport.getHttpTransport().getRequestMethod() : null;
        } else if ("http.request.path".equals(variable)) {
            return transport.isHttp() ? transport.getHttpTransport().getRequestPath() : null;
        } else if ("interfacealias".equals(variable)) {
            return transport.getInterfaceAlias();
        } else if ("ishttp".equals(variable)) {
            return transport.isHttp();
        }
        return null;
    }
}
