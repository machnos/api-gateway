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
 * Class representing the security that is used at the transport level.
 */
public interface Security {

    /**
     * Gives the name of the cipher suite that is used.
     *
     * @return The name of the cipher suite, or <ocde>null</ocde> when no transport security was applied.
     */
    String getCipherSuite();

    /**
     * Gives the name of the protocol that is used.
     *
     * @return The name of the protocol, or <code>null</code> when no transport security was applied.
     */
    String getProtocol();

    /**
     * Gives the remote certificate that is sent by the remote application during the handshake.
     *
     * @return The remote certificate, or <code>null</code> when the remote application didn't sent a certificate.
     */
    X509Certificate getRemoteCertificate();

    /**
     * Gives the chain of remote certificates that is sent by the remote application during the handshake.
     *
     * @return The remote certificates, or <code>null</code> when the remote application didn't sent any certificate.
     */
    X509Certificate[] getRemoteCertificateChain();

    /**
     * Gives the local certificate that is sent by the Machnos Api Gateway to the remote application during the handshake.
     *
     * @return The local certificate, or <code>null</code> when the Machnos Api Gateway didn't sent a certificate.
     */
    X509Certificate getLocalCertificate();

    /**
     * Gives the chain of local certificates that is sent by the Machnos Api Gateway to tehe remote application during the handshake.
     *
     * @return The remote certificates, or <code>null</code> when the Machnos Api Gateway didn't sent any certificate.
     */
    X509Certificate[] getLocalCertificateChain();
}
