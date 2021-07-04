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

import com.machnos.api.gateway.server.domain.transport.x509.X500Name;
import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the <code>X509CertificateVariableHandler</code> class.
 */
public class X509CertificateVariableHandlerTest extends AbstractVariableHandlerTest<X509CertificateVariableHandler, X509Certificate> {

    private final String issuerDN = "CN=MachnosIssuer";
    private final String subjectDN = "CN=Machnos";
    private final Date notBefore = Date.from(Instant.now().minus(1, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS));
    private final Date notAfter = Date.from(Instant.now().plus(1, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.SECONDS));
    private final BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

    @Override
    protected X509CertificateVariableHandler getHandlerInstance() {
        return new X509CertificateVariableHandler();
    }

    @Override
    protected X509Certificate getObjectToHandle() {
        return new X509Certificate(createCertificate());
    }

    /**
     * Test getting the subject instance.
     */
    @Test
    public void testGetSubject() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        final var x500Name = (X500Name)variableHandler.getValue(X509CertificateVariableHandler.PREFIX_SUBJECT, certificate);
        assertEquals(this.subjectDN, x500Name.getDN());
    }

    /**
     * Test getting the issuer instance.
     */
    @Test
    public void testGetIssuer() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        final var x500Name = (X500Name)variableHandler.getValue(X509CertificateVariableHandler.PREFIX_ISSUER, certificate);
        assertEquals(this.issuerDN, x500Name.getDN());
    }

    /**
     * Test getting the key instance.
     */
    @Test
    public void testGetKey() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        final var key = certificate.getPublicKey();
        assertEquals(key, variableHandler.getValue(X509CertificateVariableHandler.PREFIX_KEY, certificate));
    }

    /**
     * Test getting the not before instance.
     */
    @Test
    public void testGetNotBefore() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(this.notBefore, variableHandler.getValue(X509CertificateVariableHandler.NOT_BEFORE, certificate));
    }

    /**
     * Test getting the not after instance.
     */
    @Test
    public void testGetNotAfter() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(this.notAfter, variableHandler.getValue(X509CertificateVariableHandler.NOT_AFTER, certificate));
    }

    /**
     * Test getting the serial value.
     */
    @Test
    public void testGetSerial() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        final var hexSerial = new String(Hex.encode(this.serial.toByteArray()));
        assertEquals(hexSerial, variableHandler.getValue(X509CertificateVariableHandler.SERIAL, certificate));
    }

    /**
     * Test getting the version value.
     */
    @Test
    public void testGetVersion() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(certificate.getVersionNumber(), (Integer) variableHandler.getValue(X509CertificateVariableHandler.VERSION, certificate));
    }

    /**
     * Test getting the MD5 value.
     */
    @Test
    public void testGetMD5() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(certificate.getMD5(), variableHandler.getValue(X509CertificateVariableHandler.MD5, certificate));
    }

    /**
     * Test getting the SHA1 value.
     */
    @Test
    public void testGetSHA1() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(certificate.getSHA1(), variableHandler.getValue(X509CertificateVariableHandler.SHA1, certificate));
    }

    /**
     * Test getting the SHA256 value.
     */
    @Test
    public void testGetSHA256() {
        final var variableHandler = getHandlerInstance();
        final var certificate = getObjectToHandle();
        assertEquals(certificate.getSHA256(), variableHandler.getValue(X509CertificateVariableHandler.SHA256, certificate));
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
            final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
            final var certificateBuilder =
                    new JcaX509v3CertificateBuilder(new org.bouncycastle.asn1.x500.X500Name(this.issuerDN),
                            this.serial,
                            this.notBefore,
                            this.notAfter,
                            new org.bouncycastle.asn1.x500.X500Name(this.subjectDN),
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
