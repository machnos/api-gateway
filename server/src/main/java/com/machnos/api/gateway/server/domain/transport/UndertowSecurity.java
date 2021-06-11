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
import io.undertow.server.HttpServerExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLPeerUnverifiedException;
import java.util.Arrays;

/**
 * <code>Security</code> implementation that is backed by the Undertow <code>HttpServerExchange</code>.
 */
public class UndertowSecurity implements Security {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The Undertow <code>HttpServerExchange</code>.
     */
    private final HttpServerExchange httpServerExchange;

    /**
     * Constructs a new <code>UndertowSecurity</code> instance.
     *
     * @param httpServerExchange The Undertow <code>HttpServerExchange</code> that is wrapped by this class.
     */
    public UndertowSecurity(HttpServerExchange httpServerExchange) {
        this.httpServerExchange = httpServerExchange;
    }

    @Override
    public String getCipherSuite() {
        if (this.httpServerExchange.getConnection().getSslSession() == null) {
            return null;
        }
        return this.httpServerExchange.getConnection().getSslSession().getCipherSuite();
    }

    @Override
    public String getProtocol() {
        if (this.httpServerExchange.getConnection().getSslSession() == null) {
            return null;
        }
        return this.httpServerExchange.getConnection().getSslSession().getProtocol();
    }

    @Override
    public X509Certificate getRemoteCertificate() {
        var chain = getRemoteCertificateChain();
        if (chain == null) {
            return null;
        }
        return chain[0];
    }

    @Override
    public X509Certificate[] getRemoteCertificateChain() {
        if (this.httpServerExchange.getConnection().getSslSession() == null) {
            return null;
        }
        try {
            final var certificates = this.httpServerExchange.getConnection().getSslSession().getPeerCertificates();
            return Arrays.stream(certificates).map(X509Certificate::new).toArray(X509Certificate[]::new);
        } catch (SSLPeerUnverifiedException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
            return null;
        }
    }

    @Override
    public X509Certificate getLocalCertificate() {
        var chain = getLocalCertificateChain();
        if (chain == null) {
            return null;
        }
        return chain[0];
    }

    @Override
    public X509Certificate[] getLocalCertificateChain() {
        if (this.httpServerExchange.getConnection().getSslSession() == null) {
            return null;
        }
        final var certificates = this.httpServerExchange.getConnection().getSslSession().getLocalCertificates();
        if (certificates == null) {
            return null;
        }
        return Arrays.stream(certificates).map(X509Certificate::new).toArray(X509Certificate[]::new);
    }
}
