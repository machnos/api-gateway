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

public class RequireTransportSecurity extends AbstractFunction {

    private boolean remoteCertificateRequired;

    public RequireTransportSecurity() {
        super("RequireTransportSecurity");
    }

    @Override
    public Result execute(ExecutionContext executionContext) {
        if (!executionContext.getTransport().isSecure()) {
            return Result.FAILED;
        }
        final var remoteCertificate = executionContext.getTransport().getSecurity().getRemoteCertificate();
        if (executionContext.getTransport().getSecurity().getRemoteCertificate() == null) {
            return null;
        }
        final var account = new Account(remoteCertificate.getSubject().getCN(), new X509CertificateCredentials(remoteCertificate));
        executionContext.setAccount(account);
        return Result.SUCCESS;
    }

    public RequireTransportSecurity setRemoteCertificateRequired(boolean remoteCertificateRequired) {
        this.remoteCertificateRequired = remoteCertificateRequired;
        return this;
    }

    public boolean isRemoteCertificateRequired() {
        return this.remoteCertificateRequired;
    }
}
