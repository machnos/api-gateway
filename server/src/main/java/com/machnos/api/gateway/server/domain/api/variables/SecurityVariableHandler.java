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

import com.machnos.api.gateway.server.domain.transport.Security;
import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>Security</code> instance.
 */
public class SecurityVariableHandler extends AbstractVariableHandler<Security> {

    /**
     * The prefix for the remote certificate object.
     */
    public static final String PREFIX_REMOTE_CERTIFICATE = "remotecertificate" + AbstractVariableHandler.OBJECT_DIVIDER;
    /**
     * The length of the prefix for the remote certificate object.
     */
    public static final int PREFIX_REMOTE_CERTIFICATE_LENGTH = PREFIX_REMOTE_CERTIFICATE.length();
    /**
     * The prefix for the local certificate object.
     */
    public static final String PREFIX_LOCAL_CERTIFICATE = "localcertificate" + AbstractVariableHandler.OBJECT_DIVIDER;
    /**
     * The length of the prefix for the local certificate object.
     */
    public static final int PREFIX_LOCAL_CERTIFICATE_LENGTH = PREFIX_LOCAL_CERTIFICATE.length();
    /**
     * The cipher suite variable.
     */
    public static final String CIPHER_SUITE = "ciphersuite";
    /**
     * The protocol variable.
     */
    public static final String PROTOCOL = "protocol";
    /**
     * The local certificate chain variable.
     */
    public static final String LOCAL_CERTIFICATE_CHAIN = "localcertificatechain";
    /**
     * The remote certificate chain variable.
     */
    public static final String REMOTE_CERTIFICATE_CHAIN = "remotecertificatechain";

    /**
     * The <code>VariableHandler</code> that can handle values for the <code>X509CertificateVariableHandler</code> objects.
     */
    private final X509CertificateVariableHandler x509CertificateVariableHandler = new X509CertificateVariableHandler();

    @Override
    public Object getValue(String variable, Security security) {
        if (variable == null || security == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return security;
        } else if (CIPHER_SUITE.equals(variable)) {
            return security.getCipherSuite();
        } else if (PROTOCOL.equals(variable)) {
            return security.getProtocol();
        } else if (variable.startsWith(REMOTE_CERTIFICATE_CHAIN)) {
            return getValueFromCertificateChain(variable, security.getRemoteCertificateChain());
        } else if (variable.startsWith(LOCAL_CERTIFICATE_CHAIN)) {
            return getValueFromCertificateChain(variable, security.getLocalCertificateChain());
        } else if (variable.startsWith(PREFIX_REMOTE_CERTIFICATE)) {
            return security.getRemoteCertificate() != null
                    ? this.x509CertificateVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_REMOTE_CERTIFICATE_LENGTH), security.getRemoteCertificate())
                    : null;
        } else if (variable.startsWith(PREFIX_LOCAL_CERTIFICATE)) {
            return security.getLocalCertificate() != null
                    ? this.x509CertificateVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_LOCAL_CERTIFICATE_LENGTH), security.getLocalCertificate())
                    : null;
        }
        return null;
    }

    /**
     * Gives the value from a <code>X509Certificate</code> array.
     *
     * @param variable The variable to retrieve.
     * @param chain The <code>X509Certificate</code> array to retrieve the value from.
     * @return The requested value, or <code>null</code> when no such value exists.
     */
    private Object getValueFromCertificateChain(String variable, X509Certificate[] chain) {
        if (chain == null) {
            return null;
        }
        if (variable.endsWith(AbstractVariableHandler.SUFFIX_COLLECTION_SIZE)) {
            return chain.length;
        }
        final var matcher = AbstractVariableHandler.COLLECTION_INDEX_PATTERN.matcher(variable);
        if (matcher.matches()) {
            final int index = Integer.parseInt(matcher.group(2));
            if (index < 0 || index >= chain.length) {
                return null;
            }
            return chain[index];
        }
        return chain;
    }
}
