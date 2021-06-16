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

import com.machnos.api.gateway.server.domain.transport.x509.X509Certificate;
import org.bouncycastle.util.encoders.Hex;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>X509Certificate</code> instance.
 */
public class X509CertificateVariableHandler extends AbstractVariableHandler<X509Certificate> {

    /**
     * The prefix for the subject object.
     */
    public static final String PREFIX_SUBJECT = "subject";
    /**
     * The length of the prefix for the subject object.
     */
    private static final int PREFIX_SUBJECT_LENGTH = PREFIX_SUBJECT.length();
    /**
     * The prefix for the issuer object.
     */
    public static final String PREFIX_ISSUER = "issuer";
    /**
     * The length of the prefix for the issuer object.
     */
    private static final int PREFIX_ISSUER_LENGTH = PREFIX_ISSUER.length();
    /**
     * The prefix for the <code>PublicKey</code> object.
     */
    public static final String PREFIX_KEY = "key";
    /**
     * The length of the prefix for the <code>PublicKey</code> object.
     */
    private static final int PREFIX_KEY_LENGTH = PREFIX_KEY.length();
    /**
     * The not before variable.
     */
    public static final String NOT_BEFORE = "notbefore";
    /**
     * The not after variable.
     */
    public static final String NOT_AFTER = "notafter";
    /**
     * The serial variable.
     */
    public static final String SERIAL = "serial";
    /**
     * The version variable.
     */
    public static final String VERSION = "version";
    /**
     * The SHA-256 variable.
     */
    public static final String SHA256 = "sha256";
    /**
     * The SHA-1 variable.
     */
    public static final String SHA1 = "sha1";
    /**
     * The MD-5 variable.
     */
    public static final String MD5 = "md5";

    /**
     * The <code>VariableHandler</code> that can handle values for the <code>X500Name</code> objects.
     */
    private final X500NameVariableHandler x500NameVariableHandler = new X500NameVariableHandler();

    /**
     * The <code>VariableHandler</code> that can handle values for the <code>X500Name</code> objects.
     */
    private final PublicKeyVariableHandler publicKeyVariableHandler = new PublicKeyVariableHandler();

    @Override
    public Object getValue(String variable, X509Certificate x509Certificate) {
        if (NO_VARIABLE.equals(variable)) {
            return x509Certificate;
        } else if (variable.startsWith(PREFIX_SUBJECT)) {
            return this.x500NameVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_SUBJECT_LENGTH), x509Certificate.getSubject());
        } else if (variable.startsWith(PREFIX_ISSUER)) {
            return this.x500NameVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_ISSUER_LENGTH), x509Certificate.getIssuer());
        } else if (variable.startsWith(PREFIX_KEY)) {
            return this.publicKeyVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_KEY_LENGTH), x509Certificate.getPublicKey());
        } else if (NOT_BEFORE.equals(variable)) {
            return x509Certificate.getNotBefore();
        } else if (NOT_AFTER.equals(variable)) {
            return x509Certificate.getNotAfter();
        } else if (SERIAL.equals(variable)) {
            return new String(Hex.encode(x509Certificate.getSerialNumber().toByteArray()));
        } else if (VERSION.equals(variable)) {
            return x509Certificate.getVersionNumber();
        } else if (SHA256.equals(variable)) {
            return x509Certificate.getSHA256();
        } else if (SHA1.equals(variable)) {
            return x509Certificate.getSHA1();
        } else if (MD5.equals(variable)) {
            return x509Certificate.getMD5();
        }
        return null;
    }
}
