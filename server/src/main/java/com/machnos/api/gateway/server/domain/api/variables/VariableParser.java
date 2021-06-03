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

import com.machnos.api.gateway.server.domain.api.Api;
import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.RequestURL;
import com.machnos.api.gateway.server.domain.transport.Security;
import com.machnos.api.gateway.server.domain.transport.Transport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

public class VariableParser {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();


    private static final String VARIABLE_PREFIX = "${";
    private static final int VARIABLE_PREFIX_LENGTH = VARIABLE_PREFIX.length();
    private static final String VARIABLE_SUFFIX = "}";

    private static final String ACCOUNT_VARIABLE_PREFIX = "account.";
    private static final int ACCOUNT_VARIABLE_PREFIX_LENGTH = ACCOUNT_VARIABLE_PREFIX.length();
    private static final String API_VARIABLE_PREFIX = "api.";
    private static final int API_VARIABLE_PREFIX_LENGTH = API_VARIABLE_PREFIX.length();
    private static final String TRANSPORT_VARIABLE_PREFIX = "transport.";
    private static final int TRANSPORT_VARIABLE_PREFIX_LENGTH = TRANSPORT_VARIABLE_PREFIX.length();
    private static final String SECURITY_VARIABLE_PREFIX = "security.";
    private static final int SECURITY_VARIABLE_PREFIX_LENGTH = SECURITY_VARIABLE_PREFIX.length();
    private static final String REQUEST_VARIABLE_PREFIX = "request.";
    private static final int REQUEST_VARIABLE_PREFIX_LENGTH = REQUEST_VARIABLE_PREFIX.length();
    private static final String RESPONSE_VARIABLE_PREFIX = "response.";
    private static final int RESPONSE_VARIABLE_PREFIX_LENGTH = RESPONSE_VARIABLE_PREFIX.length();
    private static final String HEADER_VARIABLE_PREFIX = "header.";
    private static final int HEADER_VARIABLE_PREFIX_LENGTH = HEADER_VARIABLE_PREFIX.length();
    private static final String HEADERS_VARIABLE_PREFIX = "headers.";
    private static final int HEADERS_VARIABLE_PREFIX_LENGTH = HEADERS_VARIABLE_PREFIX.length();
    private static final String COLLECTION_VARIABLE_SUFFIX_FIRST = ".first";
    private static final int COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH = COLLECTION_VARIABLE_SUFFIX_FIRST.length();


    public String parse(String input, ExecutionContext executionContext) {
        if (input == null) {
            return null;
        }
        var result = input;
        while (true) {
            final var startIx = result.lastIndexOf(VARIABLE_PREFIX);
            if (startIx == -1) {
                return result;
            }
            final var endIx = result.indexOf(VARIABLE_SUFFIX, startIx + VARIABLE_PREFIX_LENGTH);
            if (endIx == -1) {
                return result;
            }
            final var variableName = result.substring(startIx + VARIABLE_PREFIX_LENGTH, endIx);
            final var variableValue = getValue(variableName, executionContext);
            result = result.substring(0, startIx) + (variableValue != null ? variableValue.toString() : "") + result.substring(endIx + 1);
        }
    }

    private Object getValue(String variableName, ExecutionContext executionContext) {
        if (variableName == null) {
            return null;
        }
        final var variable = variableName.toLowerCase();
        if (variable.startsWith(REQUEST_VARIABLE_PREFIX)) {
            return executionContext.getRequestMessage() != null ? getMessageValue(variable.substring(REQUEST_VARIABLE_PREFIX_LENGTH), executionContext.getRequestMessage()) : null;
        } else if (variable.startsWith(RESPONSE_VARIABLE_PREFIX)) {
            return executionContext.getResponseMessage() != null ? getMessageValue(variable.substring(RESPONSE_VARIABLE_PREFIX_LENGTH), executionContext.getResponseMessage()) : null;
        } else if (variable.startsWith(ACCOUNT_VARIABLE_PREFIX)) {
            return executionContext.getAccount() != null ? getAccountValue(variable.substring(ACCOUNT_VARIABLE_PREFIX_LENGTH), executionContext.getAccount()) : null;
        } else if (variable.startsWith(TRANSPORT_VARIABLE_PREFIX)) {
            return executionContext.getTransport() != null ? getTransportValue(variable.substring(TRANSPORT_VARIABLE_PREFIX_LENGTH), executionContext.getTransport()) : null;
        } else if (variable.startsWith(API_VARIABLE_PREFIX)) {
            return executionContext.getApi() != null ? getApiValue(variable.substring(API_VARIABLE_PREFIX_LENGTH), executionContext.getApi()) : null;
        }
        return null;
    }

    private Object getMessageValue(String variable, Message message) {
        if ("body".equals(variable)) {
            return message.getBody();
        } else if (variable.startsWith(HEADER_VARIABLE_PREFIX)) {
            if (variable.endsWith(COLLECTION_VARIABLE_SUFFIX_FIRST)) {
                var headerName = variable.substring(HEADER_VARIABLE_PREFIX_LENGTH, variable.length() - COLLECTION_VARIABLE_SUFFIX_FIRST_LENGTH);
                var values = message.getHeaders().get(headerName);
                if (values == null || values.size() == 0) {
                    return null;
                }
                return values.get(0);
            }
            return message.getHeaders().get(variable.substring(HEADER_VARIABLE_PREFIX_LENGTH));
        } else if ("ishttp".equals(variable)) {
            return message.isHttp();
        }
        return null;
    }

    private Object getAccountValue(String variable, Account account) {
        if ("username".equals(variable)) {
            return account.getUsername();
        }
        return null;
    }

    private Object getTransportValue(String variable, Transport transport) {
        if ("http.request.method".equals(variable)) {
            return transport.isHttp() ? transport.getHttpTransport().getRequestMethod() : null;
        } else if (variable.startsWith("http.request.url")) {
            return transport.isHttp() ? getRequestURLValue(variable.substring("http.request.url".length()), transport.getHttpTransport().getRequestURL()) : null;
        } else if (variable.equals("statuscode")) {
            return transport.isHttp() ? transport.getHttpTransport().getStatusCode() : null;
        } else if (variable.startsWith(SECURITY_VARIABLE_PREFIX)) {
            return getSecurityValue(variable.substring(SECURITY_VARIABLE_PREFIX_LENGTH), transport.getSecurity());
        } else if ("http.ishttp09".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp09();
        } else if ("http.ishttp10".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp10();
        } else if ("http.ishttp11".equals(variable)) {
            return transport.isHttp() && transport.getHttpTransport().isHttp11();
        } else if ("interfacealias".equals(variable)) {
            return transport.getInterfaceAlias();
        } else if ("ishttp".equals(variable)) {
            return transport.isHttp();
        } else if ("issecure".equals(variable)) {
            return transport.isSecure();
        }
        return null;
    }

    private Object getApiValue(String variable, Api api) {
        if ("name".equals(variable)) {
            return api.getName();
        } else if ("contextroot".equals(variable)) {
            return api.getContextRoot();
        }
        return null;
    }

    private Object getRequestURLValue(String variable, RequestURL requestURL) {
        if (variable.equals("")) {
            return requestURL;
        } else if (".scheme".equals(variable)) {
            return requestURL.getScheme();
        } else if (".host".equals(variable)) {
            return requestURL.getHost();
        } else if (".port".equals(variable)) {
            return requestURL.getPort();
        } else if (".path".equals(variable)) {
            return requestURL.getPath();
        } else if (".query".equals(variable)) {
            return requestURL.getQuery();
        } else if (".fragment".equals(variable)) {
            return requestURL.getFragment();
        } else if (variable.startsWith(".query.")) {
            return requestURL.getQueryParameter(variable.substring(".query.".length()));
        }
        return null;
    }

    private Object getSecurityValue(String variable, Security security) {
        if ("ciphersuite".equals(variable)) {
            return security.getCipherSuite();
        } else if ("protocol".equals(variable)) {
            return security.getProtocol();
        } else if (variable.startsWith("remotecertificate")) {
            return security.getRemoteCertificate() != null ? getCertificateValue(variable.substring("remoteCertificate".length()), security.getRemoteCertificate()) : null;
        } else if (variable.startsWith("localcertificate")) {
            return security.getLocalCertificate() != null ? getCertificateValue(variable.substring("localcertificate".length()), security.getLocalCertificate()) : null;
        }
        return null;
    }

    private Object getCertificateValue(String variable, Certificate certificate) {
        if (variable.equals("")) {
            return certificate;
        } else if (certificate instanceof X509Certificate) {
            try {
                final var certificateHolder = new JcaX509CertificateHolder((X509Certificate) certificate);
                if (variable.startsWith(".subject.")) {
                    return getX500NameValue(variable.substring(".subject.".length()), certificateHolder.getSubject());
                } else if (variable.startsWith(".issuer.")) {
                    return getX500NameValue(variable.substring(".issuer.".length()), certificateHolder.getIssuer());
                } else if (".notbefore".equals(variable)) {
                    return certificateHolder.getNotBefore();
                } else if (".notafter".equals(variable)) {
                    return certificateHolder.getNotAfter();
                } else if (".serial".equals(variable)) {
                    return new String(Hex.encode(certificateHolder.getSerialNumber().toByteArray()));
                } else if (".version".equals(variable)) {
                    return certificateHolder.getVersionNumber();
                } else if (".sha256".equals(variable)) {
                    final var encoded = certificateHolder.getEncoded();
                    final var sha256 = MessageDigest.getInstance("SHA-256");
                    return new String(Hex.encode(sha256.digest(encoded)));
                } else if (".sha1".equals(variable)) {
                    final var encoded = certificateHolder.getEncoded();
                    final var sha1 = MessageDigest.getInstance("SHA1");
                    return new String(Hex.encode(sha1.digest(encoded)));
                } else if (".md5".equals(variable)) {
                    final var encoded = certificateHolder.getEncoded();
                    final var md5 = MessageDigest.getInstance("MD5");
                    return new String(Hex.encode(md5.digest(encoded)));
                } else if (".key.algorithm".equals(variable)) {
                    return certificate.getPublicKey().getAlgorithm();
                } else if (".key.size".equals(variable)) {
                    if (certificate.getPublicKey() instanceof RSAPublicKey) {
                        return ((RSAPublicKey) certificate.getPublicKey()).getModulus().bitLength();
                    } else if (certificate.getPublicKey() instanceof DSAPublicKey) {
                        return ((DSAPublicKey) certificate.getPublicKey()).getY().bitLength();
                    }
                }
            } catch (CertificateEncodingException | IOException | NoSuchAlgorithmException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug(e.getMessage(), e);
                }
                return null;
            }
        }
        return null;
    }

    private Object getX500NameValue(String variable, X500Name subject) {
        if ("dn".equals(variable)) {
            return subject.toString();
        } else if ("cn".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.CN);
        } else if ("c".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.C);
        } else if ("o".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.O);
        } else if ("ou".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.OU);
        } else if ("l".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.L);
        } else if ("st".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.ST);
        } else if ("street".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.STREET);
        } else if ("dc".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.DC);
        } else if ("uid".equals(variable)) {
            return getValueFromX500Name(subject, BCStyle.UID);
        }
        return null;
    }

    private String getValueFromX500Name(X500Name x500Name, ASN1ObjectIdentifier objectIdentifier) {
        final var  rdNs = x500Name.getRDNs(objectIdentifier);
        if (rdNs == null || rdNs.length == 0) {
            return null;
        }
        return IETFUtils.valueToString(rdNs[0].getFirst().getValue());
    }
}
