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

package com.machnos.api.gateway.server.http;

import com.machnos.api.gateway.server.configuration.HttpInterface;
import com.machnos.api.gateway.server.domain.MachnosException;
import com.machnos.api.gateway.server.domain.keystore.FileSystemKeyStoreWrapper;
import com.machnos.api.gateway.server.domain.keystore.KeyStoreWrapper;
import com.machnos.api.gateway.server.domain.management.gui.Pac4jConfigFactory;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
import io.undertow.util.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.undertow.handler.CallbackHandler;
import org.pac4j.undertow.handler.SecurityHandler;
import org.xnio.Options;
import org.xnio.Sequence;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Encapsulated HTTP server which is the entry point for all http calls.
 */
public class Server {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    private static final String SELF_SIGNED_CERT_ALIAS = "machnos-api-gateway-self-signed";

    /**
     * The Undertow server instance.
     */
    private final Undertow server;

    /**
     * Boolean holding the current running state of the server.
     */
    private boolean running;

    /**
     * Creates a new <code>Server</code> instance.
     *
     * @param managementInterface The http configuration for the management interface.
     */
    public Server(HttpInterface managementInterface) {
        var builder = Undertow.builder();
        if (managementInterface.keystoreLocation != null && managementInterface.tlsProtocols != null && managementInterface.tlsProtocols.length > 0) {
            if (!managementInterface.keystoreLocation.exists()) {
                var parentFile = managementInterface.keystoreLocation.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
            final var keyStoreWrapper = new FileSystemKeyStoreWrapper(
                    managementInterface.keystoreLocation,
                    KeyStoreWrapper.KeyStoreType.valueOf(managementInterface.keystoreType),
                    managementInterface.getKeystorePasswordAsCharArray()
            );
            validateSelfSignedCert(keyStoreWrapper, managementInterface.getServerEntryPasswordAsCharArray());
            try {
                if (keyStoreWrapper.getKeyStore().size() == 0) {
                    // Generate self signed cert and add it to the keystore.
                    addSelfSignedCertificate(managementInterface, keyStoreWrapper);
                }
            } catch (KeyStoreException | NoSuchAlgorithmException | CertIOException | CertificateException | OperatorCreationException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }

            try {
                final var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStoreWrapper.getKeyStore(), managementInterface.getServerEntryPasswordAsCharArray());
                final var keyManagers = keyManagerFactory.getKeyManagers();
                final var sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, null, null);
                builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                        .setSocketOption(Options.SSL_ENABLED_PROTOCOLS, Sequence.of(managementInterface.tlsProtocols));
                managementInterface.getListenInetAddresses().forEach(c -> {
                    final var pac4jConfig = new Pac4jConfigFactory("https://" + c.getHostAddress() + ":" + managementInterface.listenPort).build();
                    final var pathHandler = new PathHandler();
                    pathHandler.addExactPath("/", SecurityHandler.build(exchange -> {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Hello World");
                    }, pac4jConfig, "FormClient"));
                    pathHandler.addExactPath("/login.html", exchange -> {
                        FormClient formClient = (FormClient) pac4jConfig.getClients().findClient("FormClient").get();
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html><body>");
                        sb.append("<form action=\"").append(formClient.getCallbackUrl()).append("?client_name=FormClient\" method=\"POST\">");
                        sb.append("<input type=\"text\" name=\"username\" value=\"\" />");
                        sb.append("<p />");
                        sb.append("<input type=\"password\" name=\"password\" value=\"\" />");
                        sb.append("<p />");
                        sb.append("<input type=\"submit\" name=\"submit\" value=\"Submit\" />");
                        sb.append("</form>");
                        sb.append("</body></html>");
                        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "text/html; charset=utf-8");
                        exchange.getResponseSender().send(sb.toString());
                        exchange.endExchange();
                    });
                    pathHandler.addExactPath("/callback", CallbackHandler.build(pac4jConfig, null, true));

                    builder.addHttpsListener(
                        managementInterface.listenPort,
                        c.getHostAddress(),
                        sslContext
                    ).setHandler(new SessionAttachmentHandler(
                            pathHandler,
                            new InMemorySessionManager("SessionManager"),
                            new SessionCookieConfig()
                    ));
                });
            } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
        } else {
            builder.addHttpListener(managementInterface.listenPort, managementInterface.listenInterface);
        }
        this.server = builder.build();
    }

    /**
     * Starts the http server. If the server is already started, this method does nothing.
     */
    public synchronized void start() {
        if (isRunning()) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("Starting http server...");
        }
        this.server.start();
        this.running = true;
        if (logger.isInfoEnabled()) {
            logger.info("Http server started and listening on " + this.server.getListenerInfo().stream().map(l -> l.getProtcol() + ":/" + l.getAddress()).collect(Collectors.joining(", ")) + ".");
        }
    }

    /**
     * Stops the http server. If the server is not started, this method does nothing.
     */
    public synchronized void stop() {
        if (!isRunning()) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("Stopping http server...");
        }
        this.server.stop();
        this.running = false;
        if (logger.isInfoEnabled()) {
            logger.info("Http server stopped.");
        }
    }

    /**
     * Method that indicates if the <code>Server</code> is running or not.
     * @return <code>true</code> when the <code>Server</code> is running, <code>false</code> otherwise.
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Validate the automatically created self signed certificate if it is available in the given keystore. When the
     * certificate is expired it is removed from the keystore.
     *
     * @param keyStoreWrapper The <code>KeyStoreWrapper</code> to check for the generated server certificate.
     * @param entryPassword The password for the entry.
     */
    private void validateSelfSignedCert(KeyStoreWrapper keyStoreWrapper, char[] entryPassword) {
        try {
            final var entry = keyStoreWrapper.getKeyStore().getEntry(SELF_SIGNED_CERT_ALIAS, new KeyStore.PasswordProtection(entryPassword));
            if (entry instanceof KeyStore.PrivateKeyEntry) {
                final var pkEntry = (KeyStore.PrivateKeyEntry) entry;
                if (pkEntry.getCertificate() != null && pkEntry.getCertificate() instanceof X509Certificate) {
                    final var x509Cert = (X509Certificate) pkEntry.getCertificate();
                    try {
                        x509Cert.checkValidity();
                    } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Current certificate not valid: '" + e.getMessage() + "'. Removing it from keystore so a new cert will be generated.");
                        }
                        keyStoreWrapper.deleteEntry(SELF_SIGNED_CERT_ALIAS);
                    }
                }
            }
        } catch (NoSuchAlgorithmException | UnrecoverableEntryException | KeyStoreException e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Unable to validate auto created certificate on validity.", e);
            }
        }
    }

    /**
     * Add a self signed certificate to the keystore.
     *
     * @param httpInterface The <code>HttpInterface</code> to create the certificate for.
     * @param keyStoreWrapper The <code>KeyStoreWrapper</code> that will hold the certificate and private key.
     */
    private void addSelfSignedCertificate(HttpInterface httpInterface, FileSystemKeyStoreWrapper keyStoreWrapper) throws NoSuchAlgorithmException, CertIOException, CertificateException, OperatorCreationException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        final var keyPair = keyPairGenerator.generateKeyPair();

        final var now = Instant.now();
        final var notBefore = Date.from(now);
        final var notAfter = Date.from(now.plus(Duration.ofDays(30)));

        final var publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
        final var publicKeyExtensionUtils = new X509ExtensionUtils(new BcDigestCalculatorProvider().get(new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1)));

        final var contentSigner = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

        final var generalNames = httpInterface.getListenInetAddresses().stream()
                .map(InetAddress::getHostName)
                .distinct()
                .map(f -> new GeneralName(GeneralName.dNSName, f))
                .toArray(GeneralName[]::new);

        final var certificateBuilder =
                new JcaX509v3CertificateBuilder(new X500Name("CN=Machnos,O=Machnos,C=NL"),
                        BigInteger.valueOf(now.toEpochMilli()),
                        notBefore,
                        notAfter,
                        new X500Name("CN=" + InetAddress.getLoopbackAddress().getHostName()),
                        keyPair.getPublic())
                        .addExtension(Extension.subjectAlternativeName, true, new GeneralNames(generalNames))
                        .addExtension(Extension.subjectKeyIdentifier, false, publicKeyExtensionUtils.createSubjectKeyIdentifier(publicKeyInfo))
                        .addExtension(Extension.authorityKeyIdentifier, false, publicKeyExtensionUtils.createAuthorityKeyIdentifier(publicKeyInfo))
                        .addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.digitalSignature))
                        .addExtension(Extension.basicConstraints, true, new BasicConstraints(false))
                        .addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));

        final var cert = new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider()).getCertificate(certificateBuilder.build(contentSigner));
        keyStoreWrapper.setKeyEntry(SELF_SIGNED_CERT_ALIAS, keyPair.getPrivate(), httpInterface.getServerEntryPasswordAsCharArray(), new Certificate[] {cert});

    }
}
