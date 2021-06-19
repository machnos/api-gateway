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

import com.machnos.api.gateway.server.domain.transport.x509.PublicKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAKeyGenParameterSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test class for the <code>PublicKeyVariableHandler</code> class.
 */
public class PublicKeyVariableHandlerTest extends AbstractVariableHandlerTest<PublicKeyVariableHandler, PublicKey> {

    private final String algorithm = "RSA";
    private final int keySize = 2048;

    @Override
    protected PublicKeyVariableHandler getHandlerInstance() {
        return new PublicKeyVariableHandler();
    }

    @Override
    protected PublicKey getObjectToHandle() {
        try {
            final var keyPairGenerator = KeyPairGenerator.getInstance(this.algorithm, new BouncyCastleProvider());
            keyPairGenerator.initialize(new RSAKeyGenParameterSpec(this.keySize, RSAKeyGenParameterSpec.F0));
            final var keyPair = keyPairGenerator.generateKeyPair();
            final var subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
            return new PublicKey(subjectPublicKeyInfo);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            fail(e);
        }
        return null;
    }

    /**
     * Test getting the algorithm value.
     */
    @Test
    public void testGetHeaders() {
        final var variableHandler = getHandlerInstance();
        final var publicKey = getObjectToHandle();
        assertEquals(this.algorithm, variableHandler.getValue(PublicKeyVariableHandler.ALGORITHM, publicKey));
    }

    /**
     * Test getting the key size value.
     */
    @Test
    public void testGetKeySize() {
        final var variableHandler = getHandlerInstance();
        final var publicKey = getObjectToHandle();
        assertEquals(this.keySize, variableHandler.getValue(PublicKeyVariableHandler.SIZE, publicKey));
    }
}
