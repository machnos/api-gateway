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

import com.machnos.api.gateway.server.domain.api.Api;
import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.RequestURL;
import com.machnos.api.gateway.server.domain.transport.Security;
import com.machnos.api.gateway.server.domain.transport.Transport;
import com.machnos.api.gateway.server.domain.transport.x509.X500Name;
import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;
import org.bouncycastle.util.encoders.Hex;

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
     * The prefix of the <code>Account</code> variables.
     */
    private static final String ACCOUNT_VARIABLE_PREFIX = "account.";
    /**
     * The length of the prefix of the <code>Account</code> variables.
     */
    private static final int ACCOUNT_VARIABLE_PREFIX_LENGTH = ACCOUNT_VARIABLE_PREFIX.length();
    /**
     * The prefix of the <code>Api</code> variables.
     */
    private static final String API_VARIABLE_PREFIX = "api.";
    /**
     * The length of the prefix of the <code>Api</code> variables.
     */
    private static final int API_VARIABLE_PREFIX_LENGTH = API_VARIABLE_PREFIX.length();
    /**
     * The prefix of the <code>Transport</code> variables.
     */
    private static final String TRANSPORT_VARIABLE_PREFIX = "transport.";
    /**
     * The length of the prefix of the <code>Transport</code> variables.
     */
    private static final int TRANSPORT_VARIABLE_PREFIX_LENGTH = TRANSPORT_VARIABLE_PREFIX.length();
    /**
     * The prefix of the <code>Security</code> variables.
     */
    private static final String SECURITY_VARIABLE_PREFIX = "security.";
    /**
     * The length of the prefix of the <code>Security</code> variables.
     */
    private static final int SECURITY_VARIABLE_PREFIX_LENGTH = SECURITY_VARIABLE_PREFIX.length();
    /**
     * The prefix of the request <code>Message</code> variables.
     */
    private static final String REQUEST_VARIABLE_PREFIX = "request.";
    /**
     * The length of the prefix of the request <code>Message</code> variables.
     */
    private static final int REQUEST_VARIABLE_PREFIX_LENGTH = REQUEST_VARIABLE_PREFIX.length();
    /**
     * The prefix of the response <code>Message</code> variables.
     */
    private static final String RESPONSE_VARIABLE_PREFIX = "response.";
    /**
     * The length of the prefix of the response <code>Message</code> variables.
     */
    private static final int RESPONSE_VARIABLE_PREFIX_LENGTH = RESPONSE_VARIABLE_PREFIX.length();
    /**
     * The prefix of a header variable.
     */
    private static final String HEADER_VARIABLE_PREFIX = "header.";
    /**
     * The length of the prefix of a header variable.
     */
    private static final int HEADER_VARIABLE_PREFIX_LENGTH = HEADER_VARIABLE_PREFIX.length();
    /**
     * The suffix of the first element in a collection.
     */
    private static final String COLLECTION_VARIABLE_SUFFIX_FIRST = ".first";
    /**
     * The length of the suffix of the first element in a collection.
     */
    private static final int COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH = COLLECTION_VARIABLE_SUFFIX_FIRST.length();


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
        } else if (variable.startsWith(API_VARIABLE_PREFIX)) {
            return executionContext.getApi() != null ? getApiValue(variable.substring(API_VARIABLE_PREFIX_LENGTH), executionContext.getApi()) : null;
        }
        return executionContext.getVariables().getVariable(variableName);
    }

    /**
     * Get a variable from a <code>Message</code> instance.
     *
     * @param variable The name of the variable.
     * @param message The <code>Message</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getMessageValue(String variable, Message message) {
        if ("body".equals(variable)) {
            return message.getBody();
        } else if (variable.startsWith(HEADER_VARIABLE_PREFIX)) {
            if (variable.endsWith(COLLECTION_VARIABLE_SUFFIX_FIRST)) {
                var headerName = variable.substring(HEADER_VARIABLE_PREFIX_LENGTH, variable.length() - COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH);
                return message.getHeaders().getFirst(headerName);
            }
            return message.getHeaders().get(variable.substring(HEADER_VARIABLE_PREFIX_LENGTH));
        } else if ("ishttp".equals(variable)) {
            return message.isHttp();
        }
        return null;
    }

    /**
     * Get a variable from an <code>Account</code> instance.
     *
     * @param variable The name of the variable.
     * @param account The <code>Account</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getAccountValue(String variable, Account account) {
        if ("username".equals(variable)) {
            return account.getUsername();
        }
        return null;
    }

    /**
     * Get a variable from a <code>Transport</code> instance.
     *
     * @param variable The name of the variable.
     * @param transport The <code>Transport</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getTransportValue(String variable, Transport transport) {
        if ("http.request.method".equals(variable)) {
            return transport.isHttp() ? transport.getHttpTransport().getRequestMethod() : null;
        } else if (variable.startsWith("http.request.url")) {
            return transport.isHttp() ? getRequestURLValue(variable.substring("http.request.url".length()), transport.getHttpTransport().getRequestURL()) : null;
        } else if (variable.equals("statuscode")) {
            return transport.isHttp() ? transport.getHttpTransport().getStatusCode() : null;
        } else if (variable.startsWith(SECURITY_VARIABLE_PREFIX)) {
            return getSecurityValue(variable.substring(SECURITY_VARIABLE_PREFIX_LENGTH), transport.getSecurity());
        } else if ("http.ishttp09".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp09();
        } else if ("http.ishttp10".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp10();
        } else if ("http.ishttp11".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp11();
        } else if ("interfacealias".equals(variable)) {
            return transport.getInterfaceAlias();
        } else if ("ishttp".equals(variable)) {
            return transport.isHttp();
        } else if ("issecure".equals(variable)) {
            return transport.isSecure();
        }
        return null;
    }

    /**
     * Get a variable from an <code>Api</code> instance.
     *
     * @param variable The name of the variable.
     * @param api The <code>Api</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getApiValue(String variable, Api api) {
        if ("name".equals(variable)) {
            return api.getName();
        } else if ("contextroot".equals(variable)) {
            return api.getContextRoot();
        }
        return null;
    }

    /**
     * Get a variable from a <code>RequestURL</code> instance.
     *
     * @param variable The name of the variable.
     * @param requestURL The <code>Message</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getRequestURLValue(String variable, RequestURL requestURL) {
        if (variable.equals("")) {
            return requestURL;
        } else if (".scheme".equals(variable)) {
            return requestURL.getScheme();
        } else if (".host".equals(variable)) {
            return requestURL.getHost();
        } else if (".port".equals(variable)) {
            return requestURL.getPort();
        } else if (".path".equals(variable)) {
            return requestURL.getPath();
        } else if (".query".equals(variable)) {
            return requestURL.getQuery();
        } else if (".fragment".equals(variable)) {
            return requestURL.getFragment();
        } else if (variable.startsWith(".query.")) {
            return requestURL.getQueryParameter(variable.substring(".query.".length()));
        }
        return null;
    }

    /**
     * Get a variable from a <code>Security</code> instance.
     *
     * @param variable The name of the variable.
     * @param security The <code>Security</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getSecurityValue(String variable, Security security) {
        if ("ciphersuite".equals(variable)) {
            return security.getCipherSuite();
        } else if ("protocol".equals(variable)) {
            return security.getProtocol();
        } else if (variable.startsWith("remotecertificate")) {
            return security.getRemoteCertificate() != null ? getCertificateValue(variable.substring("remoteCertificate".length()), security.getRemoteCertificate()) : null;
        } else if (variable.startsWith("localcertificate")) {
            return security.getLocalCertificate() != null ? getCertificateValue(variable.substring("localcertificate".length()), security.getLocalCertificate()) : null;
        }
        return null;
    }

    /**
     * Get a variable from a <code>X509Certificate</code> instance.
     *
     * @param variable The name of the variable.
     * @param certificate The <code>X509Certificate</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getCertificateValue(String variable, X509Certificate certificate) {
        if (variable.equals("")) {
            return certificate;
        } else if (variable.startsWith(".subject.")) {
            return getX500NameValue(variable.substring(".subject.".length()), certificate.getSubject());
        } else if (variable.startsWith(".issuer.")) {
            return getX500NameValue(variable.substring(".issuer.".length()), certificate.getIssuer());
        } else if (".notbefore".equals(variable)) {
            return certificate.getNotBefore();
        } else if (".notafter".equals(variable)) {
            return certificate.getNotAfter();
        } else if (".serial".equals(variable)) {
            return new String(Hex.encode(certificate.getSerialNumber().toByteArray()));
        } else if (".version".equals(variable)) {
            return certificate.getVersionNumber();
        } else if (".sha256".equals(variable)) {
            return certificate.getSHA256();
        } else if (".sha1".equals(variable)) {
            return certificate.getSHA1();
        } else if (".md5".equals(variable)) {
            return certificate.getMD5();
        } else if (".key.algorithm".equals(variable)) {
            return certificate.getPublicKey().getAlgorithm();
        } else if (".key.size".equals(variable)) {
            return certificate.getPublicKey().getKeySize();
        }
        return null;
    }

    /**
     * Get a variable from a <code>X500Name</code> instance.
     *
     * @param variable The name of the variable.
     * @param x500Name The <code>X500Name</code> instance containing the values.
     * @return The value of the variable, or <code>null</code> when the variable does not exists.
     */
    private Object getX500NameValue(String variable, X500Name x500Name) {
        if ("dn".equals(variable)) {
            return x500Name.getDN();
        } else if ("cn".equals(variable)) {
            return x500Name.getCN();
        } else if ("c".equals(variable)) {
            return x500Name.getC();
        } else if ("o".equals(variable)) {
            return x500Name.getO();
        } else if ("ou".equals(variable)) {
            return x500Name.getOU();
        } else if ("l".equals(variable)) {
            return x500Name.getL();
        } else if ("st".equals(variable)) {
            return x500Name.getST();
        } else if ("street".equals(variable)) {
            return x500Name.getStreet();
        } else if ("dc".equals(variable)) {
            return x500Name.getDC();
        } else if ("uid".equals(variable)) {
            return x500Name.getUID();
        }
        return null;
    }

}
