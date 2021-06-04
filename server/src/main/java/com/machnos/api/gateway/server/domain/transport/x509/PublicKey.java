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
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import java.io.IOException;

public class PublicKey {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    private final SubjectPublicKeyInfo subjectPublicKeyInfo;

    public PublicKey(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }

    public String getAlgorithm() {
        AlgorithmIdentifier algorithm = this.subjectPublicKeyInfo.getAlgorithm();
        // Found in org.bouncycastle.cert.crmf.jcajce.CRMFHelper
        // TODO check if this can be done easier.
        if (algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            return "RSA";
        } else if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa)) {
            return "DSA";
        }
        return "UNKNOWN";
    }

    public int getKeySize() {
        try {
            AsymmetricKeyParameter keyParameter = PublicKeyFactory.createKey(this.subjectPublicKeyInfo);
            if (keyParameter instanceof final RSAKeyParameters rsaKeyParameters) {
                return rsaKeyParameters.getModulus().bitLength();
            } else if (keyParameter instanceof final DSAPublicKeyParameters dsaPublicKeyParameters) {
                return dsaPublicKeyParameters.getY().bitLength();
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
