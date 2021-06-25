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

package com.machnos.api.gateway.server.domain.transport;

import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;

/**
 * Mock <code>Security</code> implementation with setter methods for all properties.
 */
public class MockSecurity implements Security {

    /**
     * The cipher suite.
     */
    private String cipherSuite;
    /**
     * The protocol.
     */
    private String protocol;
    /**
     * The remote <code>X509Certificate</code>.
     */
    private X509Certificate remoteCertificate;
    /**
     * The remote <code>X509Certificate</code> chain.
     */
    private X509Certificate[] remoteCertificateChain;
    /**
     * The local <code>X509Certificate</code>.
     */
    private X509Certificate localCertificate;
    /**
     * The local <code>X509Certificate</code> chain.
     */
    private X509Certificate[] localCertificateChain;

    @Override
    public String getCipherSuite() {
        return this.cipherSuite;
    }

    /**
     * Sets the cipher suite.
     * @param cipherSuite The cipher suite to set.
     */
    public void setCipherSuite(String cipherSuite) {
        this.cipherSuite = cipherSuite;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Sets the protocol.
     *
     * @param protocol The protocol to set.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public X509Certificate getRemoteCertificate() {
        return this.remoteCertificate;
    }

    /**
     * Sets the remote <code>X509Certificate</code>.
     *
     * @param remoteCertificate The remote <code>X509Certificate</code>.
     */
    public void setRemoteCertificate(X509Certificate remoteCertificate) {
        this.remoteCertificate = remoteCertificate;
    }

    @Override
    public X509Certificate[] getRemoteCertificateChain() {
        return this.remoteCertificateChain;
    }

    /**
     * Sets the remote <code>X509Certificate</code> chain.
     *
     * @param remoteCertificateChain The remote <code>X509Certificate</code> chain to set.
     */
    public void setRemoteCertificateChain(X509Certificate[] remoteCertificateChain) {
        this.remoteCertificateChain = remoteCertificateChain;
    }

    @Override
    public X509Certificate getLocalCertificate() {
        return this.localCertificate;
    }

    /**
     * Sets the local <code>X509Certificate</code>.
     *
     * @param localCertificate The local <code>X509Certificate</code> to set.
     */
    public void setLocalCertificate(X509Certificate localCertificate) {
        this.localCertificate = localCertificate;
    }

    @Override
    public X509Certificate[] getLocalCertificateChain() {
        return this.localCertificateChain;
    }

    /**
     * Sets the local <code>X509Certificate</code> chain.
     *
     * @param localCertificateChain The local <code>X509Certificate</code> chain to set.
     */
    public void setLocalCertificateChain(X509Certificate[] localCertificateChain) {
        this.localCertificateChain = localCertificateChain;
    }
}
