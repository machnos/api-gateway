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

package com.machnos.api.gateway.server.domain.keystore;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Abstract super class for all <code>AbstractKeyStoreWrapper</code> subclasses
 */
public abstract class AbstractKeyStoreWrapperTest {

    /**
     * The <code>KeyStoreWrapper</code> to test.
     */
    private final KeyStoreWrapper keyStoreWrapper;

    /**
     * Constructs a new <code>AbstractKeyStoreWrapperTest</code> instance.
     * @param keyStoreWrapper The <code>KeyStoreWrapper</code> to test.
     */
    AbstractKeyStoreWrapperTest(KeyStoreWrapper keyStoreWrapper) {
        this.keyStoreWrapper = keyStoreWrapper;
    }

    /**
     * Test adding an entry to the key store wrapper.
     * @throws Exception When something went wrong.
     */
    @Test
    public void testAddEntry() throws Exception {
        final var alias = getClass().getName() + "-testAddEntry";
        var keyPair = createKeyPair();
        final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        final var certificateBuilder = createCertificateBuilder(keyPair.getPublic());
        final var cert = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateBuilder.build(contentSigner));

        // Add an entry to the wrapper class.
        this.keyStoreWrapper.setKeyEntry(alias, keyPair.getPrivate(), new char[0], new Certificate[] {cert});

        final var retrievedCert = this.keyStoreWrapper.getKeyStore().getCertificate(alias);
        assertEquals(cert, retrievedCert);

        final var retrievedKey = this.keyStoreWrapper.getKeyStore().getKey(alias, new char[0]);
        assertEquals(keyPair.getPrivate(), retrievedKey);
    }

    /**
     * Test the deletion of an entry from the key store wrapper.
     * @throws Exception When something went wrong.
     */
    @Test
    public void testDeleteEntry() throws Exception {
        final var alias = getClass().getName() + "-testDeleteEntry";
        var keyPair = createKeyPair();
        final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        final var certificateBuilder = createCertificateBuilder(keyPair.getPublic());
        final var cert = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateBuilder.build(contentSigner));

        assertEquals(0, this.keyStoreWrapper.getKeyStore().size());
        // Add an entry to the wrapper class.
        this.keyStoreWrapper.setKeyEntry(alias, keyPair.getPrivate(), new char[0], new Certificate[] {cert});
        assertEquals(1, this.keyStoreWrapper.getKeyStore().size());

        // Remove the entry
        this.keyStoreWrapper.deleteEntry(alias);
        assertEquals(0, this.keyStoreWrapper.getKeyStore().size());
    }

    /**
     * Gives the internal <code>KeyStoreWrapper</code> instance.
     * @return The <code>KeyStoreWrapper</code> that is tested.
     */
    protected KeyStoreWrapper getKeyStoreWrapper() {
        return this.keyStoreWrapper;
    }

    /**
     * Creates a RSA <code>KeyPair</code>.
     *
     * @return A <code>KeyPair</code>
     * @throws NoSuchAlgorithmException When the RSA algorithm isn't supported in the current Java runtime.
     */
    protected KeyPair createKeyPair() throws NoSuchAlgorithmException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Create a certificate that can create certificates with a validity of 30 days.
     *
     * @param publicKey The public key used to create the certificate builder.
     * @return A <code>X509v3CertificateBuilder</code> instance.
     */
    protected X509v3CertificateBuilder createCertificateBuilder(PublicKey publicKey) {
        final var now = Instant.now();
        return new JcaX509v3CertificateBuilder(
                new X500Name("CN=Machnos-Test,O=Machnos,C=NL"),
                BigInteger.valueOf(now.toEpochMilli()),
                Date.from(now),
                Date.from(now.plus(Duration.ofDays(30))),
                new X500Name("CN=" + "localhost"),
                publicKey
        );
    }
}
