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

import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the <code>FileSystemKeyStoreWrapper</code> class.
 */
public class FileSystemKeyStoreWrapperTest extends AbstractKeyStoreWrapperTest {

    /**
     * The backing file for this test case.
     */
    private static final File keyStoreFile = new File(System.getProperty("java.io.tmpdir"), "machnos.p12");

    /**
     * A dummy password.
     */
    private static final char[] password = new char[]{'d', 'u', 'm', 'm', 'y'};

    /**
     * Constructs a new <code>FileSystemKeyStoreWrapperTest</code> instance.
     */
    public FileSystemKeyStoreWrapperTest() {
        super(new FileSystemKeyStoreWrapper(keyStoreFile, KeyStoreWrapper.KeyStoreType.PKCS12, password));
    }

    @AfterEach
    @BeforeEach
    public void setup() {
        if (keyStoreFile.exists()) {
            assertTrue(keyStoreFile.delete());
        }
    }

    @Test
    public void testGetKeyStoreWhenChangedOnFileSystem() throws Exception {
        // First add an entry.
        super.testAddEntry();
        assertEquals(1, getKeyStoreWrapper().getKeyStore().size());

        // Add a certificate on the filesystem directly
        final var keyStore = KeyStore.getInstance(KeyStoreWrapper.KeyStoreType.PKCS12.name());

        try (var fileInputStream = new FileInputStream(keyStoreFile)) {
            keyStore.load(fileInputStream, password);
        }

        final var alias = getClass().getName() + "-testGetKeyStoreWhenChangedOnFileSystem";
        var keyPair = createKeyPair();
        final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());
        final var certificateBuilder = createCertificateBuilder(keyPair.getPublic());
        final var cert = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateBuilder.build(contentSigner));

        keyStore.setKeyEntry(alias, keyPair.getPrivate(), password, new Certificate[]{cert});
        // There should be 2 entries in the keystore.
        assertEquals(2, keyStore.size());

        try (var fileOutputStream = new FileOutputStream(keyStoreFile)) {
            keyStore.store(fileOutputStream, password);
        }

        // The wrapper class should also be reloaded and show 2 entries.
        assertEquals(2, getKeyStoreWrapper().getKeyStore().size());

        // Now delete the file. The wrapper should be changed as well.
        assertTrue(keyStoreFile.delete());
        assertEquals(0, getKeyStoreWrapper().getKeyStore().size());

    }
}
