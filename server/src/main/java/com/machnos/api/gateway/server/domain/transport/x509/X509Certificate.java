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

/**
 * Class representing a X509 certificate.
 */
public class X509Certificate {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * The BouncyCastle <code>JcaX509CertificateHolder</code> that is wrapped by this class.
     */
    private final JcaX509CertificateHolder certificateHolder;
    /**
     * The subject of the certificate.
     */
    private X500Name subject;
    /**
     * The issuer of the certificate.
     */
    private X500Name issuer;
    /**
     * The sha-256 digest of the certificate.
     */
    private String sha256;
    /**
     * The sha-1 digest of the certificate.
     */
    private String sha1;
    /**
     * The md-5 digest of the certificate.
     */
    private String md5;
    /**
     * The <code>PublicKey</code> part of the certificate.
     */
    private PublicKey publicKey;

    /**
     * Constructs a new <code>X509Certificate</code> instance.
     *
     * @param certificate The <code>Certificate</code> to wrap with this instance.
     */
    public X509Certificate(Certificate certificate) {
        try {
            this.certificateHolder = new JcaX509CertificateHolder((java.security.cert.X509Certificate) certificate);
        } catch (CertificateEncodingException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    /**
     * Gives the subject of the certificate.
     *
     * @return The subject of the certificate.
     */
    public X500Name getSubject() {
        if (this.subject == null) {
            this.subject = new X500Name(this.certificateHolder.getSubject());
        }
        return this.subject;
    }

    /**
     * Gives the issuer of the certificate.
     *
     * @return The issuer of the certificate.
     */
    public X500Name getIssuer() {
        if (this.issuer == null) {
            this.issuer = new X500Name(this.certificateHolder.getIssuer());
        }
        return this.issuer;
    }

    /**
     * Gives the <code>Date</code> at which this certificate becomes valid.
     *
     * @return The <code>Date</code> at which this certificate becomes valid.
     */
    public Date getNotBefore() {
        return this.certificateHolder.getNotBefore();
    }

    /**
     * Gives the <code>Date</code> at which this certificate becomes invalid.
     *
     * @return The <code>Date</code> at which this certificate becomes invalid.
     */
    public Date getNotAfter() {
        return this.certificateHolder.getNotAfter();
    }

    /**
     * Gives the serial number of the certificate.
     *
     * @return The serial number.
     */
    public BigInteger getSerialNumber() {
        return this.certificateHolder.getSerialNumber();
    }

    /**
     * Gives the version number of the certificate.
     *
     * @return The version number.
     */
    public int getVersionNumber() {
        return this.certificateHolder.getVersionNumber();
    }

    /**
     * Gives a string representation of the sha-256 digest of the certificate.
     *
     * @return The sha-256 digest of the certificate.
     */
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

    /**
     * Gives a string representation of the sha-1 digest of the certificate.
     *
     * @return The sha-1 digest of the certificate.
     */
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

    /**
     * Gives a string representation of the md-5 digest of the certificate.
     *
     * @return The md-5 digest of the certificate.
     */
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

    /**
     * Gives the <code>PublicKey</code> part of the certificate.
     *
     * @return The <code>PublicKey</code>.
     */
    public PublicKey getPublicKey() {
        if (this.publicKey == null) {
            this.publicKey = new PublicKey(this.certificateHolder.getSubjectPublicKeyInfo());
        }
        return this.publicKey;
    }

}
