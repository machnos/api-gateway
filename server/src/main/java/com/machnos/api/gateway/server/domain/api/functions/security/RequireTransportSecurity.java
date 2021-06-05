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
import com.machnos.api.gateway.server.domain.idm.X509CertificateCredentials;

/**
 * <code>Function</code> that enforces a secure transport to an <code>Api</code>.
 *
 * If the {@link #isRemoteCertificateRequired()} returns <code>true</code> the client must send a client certificate
 * during the TLS handshake (a.k.a. mutual TLS) otherwise this <code>Function</code> will fail. The information of the
 * received certificate will be extracted into an <code>Account</code> and placed on the <code>ExecutionContext</code>.
 * <b>Be aware</b> that the <code>Account</code> that will be created has not been authorized!
 *
 * Note that when the {@link #setRemoteCertificateRequired(boolean)} is set to <code>true</code> the receiving interface
 * must be configured in such a way that a client certificate is requested/required during the handshake.
 */
public class RequireTransportSecurity extends AbstractFunction {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Require Transport Security";

    /**
     * The <code>Result</code> of this <code>Function</code> in case of an insecure transport.
     */
    private static final Result RESULT_TRANSPORT_NOT_SECURE = Result.fail(FUNCTION_NAME + " - Transport not secure - 01");

    /**
     * The <code>Result</code> of this <code>Function</code> in case of a missing remote certificate.
     */
    private static final Result RESULT_REMOTE_CERTIFICATE_MISSING = Result.fail(FUNCTION_NAME + " - Remote certificate missing - 02");

    /**
     * Boolean indicating a client certificate is required on the <code>Transport</code>.
     */
    private boolean remoteCertificateRequired;

    /**
     * Constructs a new <code>RequireTransportSecurity</code> instance.
     */
    public RequireTransportSecurity() {
        super(FUNCTION_NAME);
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        if (!executionContext.getTransport().isSecure()) {
            return RESULT_TRANSPORT_NOT_SECURE;
        }
        final var remoteCertificate = executionContext.getTransport().getSecurity().getRemoteCertificate();
        if (isRemoteCertificateRequired() && remoteCertificate == null) {
            return RESULT_REMOTE_CERTIFICATE_MISSING;
        }
        final var account = new Account(remoteCertificate.getSubject().getCN(), new X509CertificateCredentials(remoteCertificate));
        executionContext.setAccount(account);
        return Result.succeed();
    }

    public RequireTransportSecurity setRemoteCertificateRequired(boolean remoteCertificateRequired) {
        this.remoteCertificateRequired = remoteCertificateRequired;
        return this;
    }

    /**
     * Boolean indicating if a client certificate is required on the <code>Transport</code> to the <code>Api</code>.
     *
     * @return <code>true</code> if the client should send a client certificate (a.k.a. mutual TLS) to the
     * <code>Api</code>, <code>false</code> otherwise.
     */
    public boolean isRemoteCertificateRequired() {
        return this.remoteCertificateRequired;
    }
}
