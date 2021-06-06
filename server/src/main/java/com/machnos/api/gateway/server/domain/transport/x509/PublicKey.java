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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.io.IOException;

/**
 * Class representing a Public Key.
 */
public class PublicKey {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The public key info.
     */
    private final SubjectPublicKeyInfo subjectPublicKeyInfo;

    /**
     * Constructs a new <code>PublicKey</code> instance based.
     *
     * @param subjectPublicKeyInfo The <code>SubjectPublicKeyInfo</code> that is wrapped by this class.
     */
    public PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }

    /**
     * Gives a string representation of the algorithm of the <code>PublicKey</code>.
     *
     * @return The name of the algorithm, or "UNKNOWN" if the algorithm is not known.
     */
    public String getAlgorithm() {
        AlgorithmIdentifier algorithm = this.subjectPublicKeyInfo.getAlgorithm();
        if (algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            return "RSA";
        } else if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            return "EC";
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Unknown algorithm: '%s'", algorithm.getAlgorithm().getId()));
        }
        return "UNKNOWN";
    }

    /**
     * Returns the bit length of the key.
     *
     * @return The bit length of the key, or -1 if it cannot be determined.
     */
    public int getKeySize() {
        try {
            AsymmetricKeyParameter keyParameter = PublicKeyFactory.createKey(this.subjectPublicKeyInfo);
            if (keyParameter instanceof final RSAKeyParameters rsaKeyParameters) {
                return rsaKeyParameters.getModulus().bitLength();
            } else if (keyParameter instanceof final ECKeyParameters ecKeyParameters) {
                return ecKeyParameters.getParameters().getN().bitLength();
            }
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Unknown key type: '%s'", keyParameter.getClass().getName()));
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
        }
        return -1;
    }
}
