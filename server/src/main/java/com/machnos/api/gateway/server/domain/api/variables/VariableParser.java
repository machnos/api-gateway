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

/**
 * A class that is capable of parsing variable names and returning their values.
 */
public class VariableParser {

    /**
     * The prefix of a variable name.
     */
    private static final String VARIABLE_PREFIX = "${";
    /**
     * The length of the prefix of a variable name.
     */
    private static final int VARIABLE_PREFIX_LENGTH = VARIABLE_PREFIX.length();
    /**
     * The suffix of a variable name.
     */
    private static final String VARIABLE_SUFFIX = "}";
    /**
     * The length of the suffix of a variable name.
     */
    private static final int VARIABLE_SUFFIX_LENGTH = VARIABLE_SUFFIX.length();
    /**
     * The the <code>Account</code> variables.
     */
    private static final String ACCOUNT = "account";
    /**
     * The prefix of the <code>Account</code> variables.
     */
    private static final String ACCOUNT_VARIABLE_PREFIX = ACCOUNT + ".";
    /**
     * The length of the prefix of the <code>Account</code> variables.
     */
    private static final int ACCOUNT_VARIABLE_PREFIX_LENGTH = ACCOUNT_VARIABLE_PREFIX.length();
    /**
     * The <code>Api</code> variables.
     */
    private static final String API = "api";
    /**
     * The prefix of the <code>Api</code> variables.
     */
    private static final String API_VARIABLE_PREFIX = API + ".";
    /**
     * The length of the prefix of the <code>Api</code> variables.
     */
    private static final int API_VARIABLE_PREFIX_LENGTH = API_VARIABLE_PREFIX.length();
    /**
     * The <code>Transport</code> variables.
     */
    private static final String TRANSPORT = "transport";
    /**
     * The prefix of the <code>Transport</code> variables.
     */
    private static final String TRANSPORT_VARIABLE_PREFIX = TRANSPORT + ".";
    /**
     * The length of the prefix of the <code>Transport</code> variables.
     */
    private static final int TRANSPORT_VARIABLE_PREFIX_LENGTH = TRANSPORT_VARIABLE_PREFIX.length();
    /**
     * The request <code>Message</code> variables.
     */
    private static final String REQUEST = "request";
    /**
     * The prefix of the request <code>Message</code> variables.
     */
    private static final String REQUEST_VARIABLE_PREFIX = REQUEST + ".";
    /**
     * The length of the prefix of the request <code>Message</code> variables.
     */
    private static final int REQUEST_VARIABLE_PREFIX_LENGTH = REQUEST_VARIABLE_PREFIX.length();
    /**
     * The response <code>Message</code> variables.
     */
    private static final String RESPONSE = "response";
    /**
     * The prefix of the response <code>Message</code> variables.
     */
    private static final String RESPONSE_VARIABLE_PREFIX = RESPONSE + ".";
    /**
     * The length of the prefix of the response <code>Message</code> variables.
     */
    private static final int RESPONSE_VARIABLE_PREFIX_LENGTH = RESPONSE_VARIABLE_PREFIX.length();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Message</code> objects.
     */
    private final MessageVariableHandler messageVariableHandler = new MessageVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Account</code> objects.
     */
    private final AccountVariableHandler accountVariableHandler = new AccountVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Transport</code> objects.
     */
    private final TransportVariableHandler transportVariableHandler = new TransportVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Api</code> objects.
     */
    private final ApiVariableHandler apiVariableHandler = new ApiVariableHandler();

    /**
     * Parse the input and replaces all variables with actual values.
     *
     * @param input The input to parse.
     * @param executionContext The <code>ExecutionContext</code> that contains all variables.
     * @return The parsed input.
     */
    public String parseAsString(String input, ExecutionContext executionContext) {
        if (input == null) {
            return null;
        }
        var result = input;
        // Loop over the result finding variable names.
        while (true) {
            // Match over the last index. This will allow us to support variables within variables because the
            // inner variable will always start after the outer variable.
            final var startIx = result.lastIndexOf(VARIABLE_PREFIX);
            if (startIx == -1) {
                // No start index, hence no variables in the result.
                return result;
            }
            final var endIx = result.indexOf(VARIABLE_SUFFIX, startIx + VARIABLE_PREFIX_LENGTH);
            if (endIx == -1) {
                // No end index, hence no variables in the result.
                return result;
            }
            final var variableName = result.substring(startIx + VARIABLE_PREFIX_LENGTH, endIx);
            final var variableValue = getValue(variableName, executionContext);
            result = result.substring(0, startIx) + (variableValue != null ? variableValue.toString() : "") + result.substring(endIx + VARIABLE_SUFFIX_LENGTH);
        }
    }

    /**
     * Get a value of a variable.
     *
     * @param variableName The name of the variable.
     * @param executionContext The <code>ExecutionContext</code> that contains all variables.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    @SuppressWarnings("unchecked")
    public <I> I getValue(String variableName, ExecutionContext executionContext) {
        if (variableName == null) {
            return null;
        }
        final var variable = variableName.toLowerCase();
        if (variable.equals(REQUEST)) {
            return (I) executionContext.getRequestMessage();
        } else if (variable.startsWith(REQUEST_VARIABLE_PREFIX)) {
            return executionContext.getRequestMessage() != null ? this.messageVariableHandler.getValue(variable.substring(REQUEST_VARIABLE_PREFIX_LENGTH), executionContext.getRequestMessage()) : null;
        } else if (variable.equals(RESPONSE)) {
            return (I) executionContext.getResponseMessage();
        } else if (variable.startsWith(RESPONSE_VARIABLE_PREFIX)) {
            return executionContext.getResponseMessage() != null ? this.messageVariableHandler.getValue(variable.substring(RESPONSE_VARIABLE_PREFIX_LENGTH), executionContext.getResponseMessage()) : null;
        } else if (variable.equals(ACCOUNT)) {
            return (I) executionContext.getAccount();
        } else if (variable.startsWith(ACCOUNT_VARIABLE_PREFIX)) {
            return executionContext.getAccount() != null ? this.accountVariableHandler.getValue(variable.substring(ACCOUNT_VARIABLE_PREFIX_LENGTH), executionContext.getAccount()) : null;
        } else if (variable.equals(TRANSPORT)) {
            return (I) executionContext.getTransport();
        } else if (variable.startsWith(TRANSPORT_VARIABLE_PREFIX)) {
            return executionContext.getTransport() != null ? this.transportVariableHandler.getValue(variable.substring(TRANSPORT_VARIABLE_PREFIX_LENGTH), executionContext.getTransport()) : null;
        } else if (variable.equals(API)) {
            return (I) executionContext.getApi();
        } else if (variable.startsWith(API_VARIABLE_PREFIX)) {
            return executionContext.getApi() != null ? this.apiVariableHandler.getValue(variable.substring(API_VARIABLE_PREFIX_LENGTH), executionContext.getApi()) : null;
        }
        return (I) executionContext.getVariables().getVariable(variableName);
    }
}
