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

import com.machnos.api.gateway.server.domain.transport.MockSecurity;
import com.machnos.api.gateway.server.domain.transport.Security;
import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the <code>SecurityVariableHandler</code> class.
 */
public class SecurityVariableHandlerTest extends AbstractVariableHandlerTest<SecurityVariableHandler, Security> {

    @Override
    protected SecurityVariableHandler getHandlerInstance() {
        return new SecurityVariableHandler();
    }

    @Override
    protected MockSecurity getObjectToHandle() {
        return new MockSecurity();
    }

    /**
     * Test getting the cipher suite value.
     */
    @Test
    public void testGetCipherSuite() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var cipherSuite = "TLS_AES_128_GCM_SHA256";
        security.setCipherSuite(cipherSuite);
        assertEquals(cipherSuite, variableHandler.getValue(SecurityVariableHandler.CIPHER_SUITE, security));
    }

    /**
     * Test getting the protocol value.
     */
    @Test
    public void testGetProtocol() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var protocol = "TLS1.3";
        security.setProtocol(protocol);
        assertEquals(protocol, variableHandler.getValue(SecurityVariableHandler.PROTOCOL, security));
    }

    /**
     * Test getting the local certificate.
     */
    @Test
    public void testGetLocalCertificate() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509Certificate = new X509Certificate(createCertificate());
        security.setLocalCertificate(x509Certificate);
        assertEquals(x509Certificate, variableHandler.getValue(SecurityVariableHandler.PREFIX_LOCAL_CERTIFICATE, security));
        security.setLocalCertificate(null);
        assertNull(variableHandler.getValue(SecurityVariableHandler.PREFIX_LOCAL_CERTIFICATE, security));
    }

    /**
     * Test getting the local certificate chain.
     */
    @Test
    public void testGetLocalCertificateChain() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), new X509Certificate(createCertificate()) };
        security.setLocalCertificateChain(x509CertificateChain);
        assertEquals(x509CertificateChain, variableHandler.getValue(SecurityVariableHandler.LOCAL_CERTIFICATE_CHAIN, security));
        security.setLocalCertificateChain(null);
        assertNull(variableHandler.getValue(SecurityVariableHandler.LOCAL_CERTIFICATE_CHAIN, security));
    }

    /**
     * Test getting the local certificate chain size.
     */
    @Test
    public void testGetLocalCertificateChainSize() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), new X509Certificate(createCertificate()) };
        security.setLocalCertificateChain(x509CertificateChain);
        assertEquals(x509CertificateChain.length, variableHandler.getValue(SecurityVariableHandler.LOCAL_CERTIFICATE_CHAIN + AbstractVariableHandler.SUFFIX_COLLECTION_SIZE, security));
    }

    /**
     * Test getting the local certificate at a certain index in the chain.
     */
    @Test
    public void testGetLocalCertificateChainAtIndex() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var cert = new X509Certificate(createCertificate());
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), cert, new X509Certificate(createCertificate()) };
        security.setLocalCertificateChain(x509CertificateChain);
        assertEquals(cert, variableHandler.getValue(SecurityVariableHandler.LOCAL_CERTIFICATE_CHAIN + "[1]", security));
        assertNull(variableHandler.getValue(SecurityVariableHandler.LOCAL_CERTIFICATE_CHAIN + "[10]", security));
    }

    /**
     * Test getting the remote certificate.
     */
    @Test
    public void testGetRemoteCertificate() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509Certificate = new X509Certificate(createCertificate());
        security.setRemoteCertificate(x509Certificate);
        assertEquals(x509Certificate, variableHandler.getValue(SecurityVariableHandler.PREFIX_REMOTE_CERTIFICATE, security));
        security.setRemoteCertificate(null);
        assertNull(variableHandler.getValue(SecurityVariableHandler.PREFIX_REMOTE_CERTIFICATE, security));
    }

    /**
     * Test getting the remote certificate chain.
     */
    @Test
    public void testGetRemoteCertificateChain() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), new X509Certificate(createCertificate()) };
        security.setRemoteCertificateChain(x509CertificateChain);
        assertEquals(x509CertificateChain, variableHandler.getValue(SecurityVariableHandler.REMOTE_CERTIFICATE_CHAIN, security));
        security.setRemoteCertificateChain(null);
        assertNull(variableHandler.getValue(SecurityVariableHandler.REMOTE_CERTIFICATE_CHAIN, security));
    }

    /**
     * Test getting the remote certificate chain size.
     */
    @Test
    public void testGetRemoteCertificateChainSize() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), new X509Certificate(createCertificate()) };
        security.setRemoteCertificateChain(x509CertificateChain);
        assertEquals(x509CertificateChain.length, variableHandler.getValue(SecurityVariableHandler.REMOTE_CERTIFICATE_CHAIN + AbstractVariableHandler.SUFFIX_COLLECTION_SIZE, security));
    }

    /**
     * Test getting the remote certificate at a certain index in the chain.
     */
    @Test
    public void testGetRemoteCertificateChainAtIndex() {
        final var variableHandler = getHandlerInstance();
        final var security = getObjectToHandle();
        final var cert = new X509Certificate(createCertificate());
        final var x509CertificateChain = new X509Certificate[] { new X509Certificate(createCertificate()), cert, new X509Certificate(createCertificate()) };
        security.setRemoteCertificateChain(x509CertificateChain);
        assertEquals(cert, variableHandler.getValue(SecurityVariableHandler.REMOTE_CERTIFICATE_CHAIN + "[1]", security));
        assertNull(variableHandler.getValue(SecurityVariableHandler.REMOTE_CERTIFICATE_CHAIN + "[10]", security));
    }


    /**
     * Constructs a new <code>Certificate</code>.
     *
     * @return A <code>Certificate</code>.
     */
    private Certificate createCertificate() {
        try {
            final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            final var keyPair = keyPairGenerator.generateKeyPair();

            final var now = Instant.now();
            final var notBefore = Date.from(now);
            final var notAfter = Date.from(now.plus(Duration.ofDays(30)));
            final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

            final var certificateBuilder =
                    new JcaX509v3CertificateBuilder(new X500Name("CN=Machnos,O=Machnos,C=NL"),
                            BigInteger.valueOf(now.toEpochMilli()),
                            notBefore,
                            notAfter,
                            new X500Name("CN=" + InetAddress.getLoopbackAddress().getHostName()),
                            keyPair.getPublic());
            return new JcaX509CertificateConverter()
                    .setProvider(new BouncyCastleProvider())
                    .getCertificate(certificateBuilder.build(contentSigner));
        } catch (CertificateException | NoSuchAlgorithmException | OperatorCreationException e) {
            fail(e);
        }
        return null;
    }

}
