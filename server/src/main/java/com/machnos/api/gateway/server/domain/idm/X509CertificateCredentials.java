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

package com.machnos.api.gateway.server.domain.idm;

import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;

/**
 * Credentials in the form of a X509 certificate
 */
public class X509CertificateCredentials implements Credentials {

    /**
     * The <code>X509Certificate</code>.
     */
    private final X509Certificate x509Certificate;

    /**
     * Constructs a new <code>X509CertificateCredentials</code> instance.
     *
     * @param x509Certificate The <code>X509Certificate</code>.
     */
    public X509CertificateCredentials(X509Certificate x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    /**
     * Gives the <code>X509Certificate</code>.
     *
     * @return The <code>X509Certificate</code>.
     */
    public X509Certificate getX509Certificate() {
        return this.x509Certificate;
    }
}