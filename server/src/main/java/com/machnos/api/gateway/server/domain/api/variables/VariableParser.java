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
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.RequestURL;
import com.machnos.api.gateway.server.domain.transport.x509.PublicKey;
import com.machnos.api.gateway.server.domain.transport.x509.X500Name;
import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;

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
    private static final String ACCOUNT_VARIABLE_PREFIX = ACCOUNT + VariableHandler.OBJECT_DIVIDER;
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
    private static final String API_VARIABLE_PREFIX = API + VariableHandler.OBJECT_DIVIDER;
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
    private static final String TRANSPORT_VARIABLE_PREFIX = TRANSPORT + VariableHandler.OBJECT_DIVIDER;
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
    private static final String REQUEST_VARIABLE_PREFIX = REQUEST + VariableHandler.OBJECT_DIVIDER;
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
    private static final String RESPONSE_VARIABLE_PREFIX = RESPONSE + VariableHandler.OBJECT_DIVIDER;
    /**
     * The length of the prefix of the response <code>Message</code> variables.
     */
    private static final int RESPONSE_VARIABLE_PREFIX_LENGTH = RESPONSE_VARIABLE_PREFIX.length();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Account</code> objects.
     */
    private static final AccountVariableHandler accountVariableHandler = new AccountVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Api</code> objects.
     */
    private static final ApiVariableHandler apiVariableHandler = new ApiVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Message</code> objects.
     */
    private static final MessageVariableHandler messageVariableHandler = new MessageVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>PublicKey</code> objects.
     */
    private static final PublicKeyVariableHandler publicKeyVariableHandler = new PublicKeyVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>RequestURL</code> objects.
     */
    private static final RequestURLVariableHandler requestURLVariableHandler = new RequestURLVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Transport</code> objects.
     */
    private static final TransportVariableHandler transportVariableHandler = new TransportVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>X500Name</code> objects.
     */
    private static final X500NameVariableHandler x500NameVariableHandler = new X500NameVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>X509Certificate</code> objects.
     */
    private static final X509CertificateVariableHandler x509CertificateVariableHandler = new X509CertificateVariableHandler();

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
     * @param <I> The return type.
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
            return executionContext.getRequestMessage() != null ? messageVariableHandler.getValue(variable.substring(REQUEST_VARIABLE_PREFIX_LENGTH), executionContext.getRequestMessage()) : null;
        } else if (variable.equals(RESPONSE)) {
            return (I) executionContext.getResponseMessage();
        } else if (variable.startsWith(RESPONSE_VARIABLE_PREFIX)) {
            return executionContext.getResponseMessage() != null ? messageVariableHandler.getValue(variable.substring(RESPONSE_VARIABLE_PREFIX_LENGTH), executionContext.getResponseMessage()) : null;
        } else if (variable.equals(ACCOUNT)) {
            return (I) executionContext.getAccount();
        } else if (variable.startsWith(ACCOUNT_VARIABLE_PREFIX)) {
            return executionContext.getAccount() != null ? accountVariableHandler.getValue(variable.substring(ACCOUNT_VARIABLE_PREFIX_LENGTH), executionContext.getAccount()) : null;
        } else if (variable.equals(TRANSPORT)) {
            return (I) executionContext.getTransport();
        } else if (variable.startsWith(TRANSPORT_VARIABLE_PREFIX)) {
            return executionContext.getTransport() != null ? transportVariableHandler.getValue(variable.substring(TRANSPORT_VARIABLE_PREFIX_LENGTH), executionContext.getTransport()) : null;
        } else if (variable.equals(API)) {
            return (I) executionContext.getApi();
        } else if (variable.startsWith(API_VARIABLE_PREFIX)) {
            return executionContext.getApi() != null ? apiVariableHandler.getValue(variable.substring(API_VARIABLE_PREFIX_LENGTH), executionContext.getApi()) : null;
        }
        return getCustomValue(variable, executionContext);
    }

    /**
     * Get the value of a custom variable.
     *
     * @param variableName The name of the variable.
     * @param executionContext The <code>ExecutionContext</code> that holds all custom variables.
     * @param <I> The return type.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    @SuppressWarnings("unchecked")
    private <I> I getCustomValue(String variableName, ExecutionContext executionContext) {
        var result = executionContext.getVariables().getVariable(variableName);
        if (result == null) {
            // Check if we request an attribute inside an object.
            // If, for example, a RequestURL is stored under ${custom.url}, then ${custom.url.host} would return null.
            var startIx = 0;
            var attributeStartIx = 0;
            while(result == null) {
                // Loop through the variable parts to see if we can find a value.
                attributeStartIx = variableName.indexOf(VariableHandler.OBJECT_DIVIDER, startIx);
                if (attributeStartIx == -1) {
                    return null;
                }
                final var parentObjectVariableName = variableName.substring(0, attributeStartIx);
                result = executionContext.getVariables().getVariable(parentObjectVariableName);
                startIx += attributeStartIx + VariableHandler.OBJECT_DIVIDER_LENGTH;
            }
            final var attributeName = variableName.substring(attributeStartIx + VariableHandler.OBJECT_DIVIDER_LENGTH);
            if (result instanceof Message message) {
                return messageVariableHandler.getValue(attributeName, message);
            } else if (result instanceof PublicKey publicKey) {
                return publicKeyVariableHandler.getValue(attributeName, publicKey);
            } else if (result instanceof RequestURL requestURL) {
                return requestURLVariableHandler.getValue(attributeName, requestURL);
            } else if (result instanceof X500Name x500Name) {
                return x500NameVariableHandler.getValue(attributeName, x500Name);
            } else if (result instanceof X509Certificate x509Certificate) {
                return x509CertificateVariableHandler.getValue(attributeName, x509Certificate);
            }
        }
        return (I) result;
    }
}
