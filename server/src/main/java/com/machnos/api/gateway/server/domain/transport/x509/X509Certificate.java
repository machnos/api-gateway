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

import com.machnos.api.gateway.server.domain.MachnosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Date;

public class X509Certificate {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    private final JcaX509CertificateHolder certificateHolder;
    private X500Name subject;
    private X500Name issuer;
    private String sha256;
    private String sha1;
    private String md5;
    private PublicKey publicKey;

    public X509Certificate(Certificate certificate) {
        try {
            this.certificateHolder = new JcaX509CertificateHolder((java.security.cert.X509Certificate) certificate);
        } catch (CertificateEncodingException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    public X500Name getSubject() {
        if (this.subject == null) {
            this.subject = new X500Name(this.certificateHolder.getSubject());
        }
        return this.subject;
    }

    public X500Name getIssuer() {
        if (this.issuer == null) {
            this.issuer = new X500Name(this.certificateHolder.getIssuer());
        }
        return this.issuer;
    }

    public Date getNotBefore() {
        return this.certificateHolder.getNotBefore();
    }

    public Date getNotAfter() {
        return this.certificateHolder.getNotAfter();
    }

    public BigInteger getSerialNumber() {
        return this.certificateHolder.getSerialNumber();
    }

    public int getVersionNumber() {
        return this.certificateHolder.getVersionNumber();
    }

    public String getSHA256() {
        if (this.sha256 == null) {
            try {
                final var digest = MessageDigest.getInstance("SHA-256");
                this.sha256 = new String(Hex.encode(digest.digest(this.certificateHolder.getEncoded())));
            } catch (NoSuchAlgorithmException | IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return this.sha256;
    }

    public String getSHA1() {
        if (this.sha1 == null) {
            try {
                final var digest = MessageDigest.getInstance("SHA1");
                this.sha1 = new String(Hex.encode(digest.digest(this.certificateHolder.getEncoded())));
            } catch (NoSuchAlgorithmException | IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return this.sha1;
    }

    public String getMD5() {
        if (this.md5 == null) {
            try {
                final var digest = MessageDigest.getInstance("SHA1");
                this.md5 = new String(Hex.encode(digest.digest(this.certificateHolder.getEncoded())));
            } catch (NoSuchAlgorithmException | IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return this.md5;
    }

    public PublicKey getPublicKey() {
        if (this.publicKey == null) {
            this.publicKey = new PublicKey(this.certificateHolder.getSubjectPublicKeyInfo());
        }
        return this.publicKey;
    }

}
