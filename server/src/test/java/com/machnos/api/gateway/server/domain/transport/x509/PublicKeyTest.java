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

package com.machnos.api.gateway.server.domain.transport.x509;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the <code>PublicKey</code> class.
 */
public class PublicKeyTest {

    @Test
    public void testRSAPublicKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        final var algorithm = "RSA";
        final var keySize = 4096;
        final var publicKey = createPublicKey(algorithm, new RSAKeyGenParameterSpec(keySize, RSAKeyGenParameterSpec.F0));
        assertEquals(algorithm, publicKey.getAlgorithm());
        assertEquals(keySize, publicKey.getKeySize());
    }

    @Test
    public void testECPublicKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        final var algorithm = "EC";
        final var keySize = 384;
        final var publicKey = createPublicKey(algorithm, new ECGenParameterSpec("P-" + keySize));
        assertEquals(algorithm, publicKey.getAlgorithm());
        assertEquals(keySize, publicKey.getKeySize());
    }

    private PublicKey createPublicKey(String algorithm, AlgorithmParameterSpec algorithmParameterSpec) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        final var keyPairGenerator = KeyPairGenerator.getInstance(algorithm, new BouncyCastleProvider());
        keyPairGenerator.initialize(algorithmParameterSpec);
        final var keyPair = keyPairGenerator.generateKeyPair();
        final var subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        return new PublicKey(subjectPublicKeyInfo);
    }
}
