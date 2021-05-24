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

import com.machnos.api.gateway.server.configuration.Configuration;
import com.machnos.api.gateway.server.configuration.HttpInterface;
import com.machnos.api.gateway.server.domain.MachnosException;
import com.machnos.api.gateway.server.domain.keystore.FileSystemKeyStoreWrapper;
import com.machnos.api.gateway.server.domain.keystore.KeyStoreWrapper;
import com.machnos.api.gateway.server.domain.management.gui.Pac4jConfigFactory;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.session.InMemorySessionManager;
import io.undertow.server.session.SessionAttachmentHandler;
import io.undertow.server.session.SessionCookieConfig;
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
import org.pac4j.core.matching.matcher.DefaultMatchers;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.undertow.handler.CallbackHandler;
import org.pac4j.undertow.handler.LogoutHandler;
import org.pac4j.undertow.handler.SecurityHandler;
import org.xnio.OptionMap;
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
     * @param configuration The configuration used to setup the <code>Server</code>.
     */
    public Server(Configuration configuration) {
        var managementInterface = configuration.management;
        var clusterName = configuration.clusterName;
        var builder = Undertow.builder();
        builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true);

        // Setup the management interface.
        if (managementInterface != null) {
            final var pac4jConfig = new Pac4jConfigFactory().build();
            final var pathHandler = new PathHandler();
            final var resourceRootPackage = "com/machnos/api/gateway/server/gui";
            pathHandler.addExactPath("/", SecurityHandler.build(new ResourceHandler(new InjectingClasspathResourceManager(resourceRootPackage, clusterName)), pac4jConfig, "FormClient", null, DefaultMatchers.SECURITYHEADERS + Pac4jConstants.ELEMENT_SEPARATOR + "MachnosCsrfToken"));
            pathHandler.addPrefixPath("/login/", new ResourceHandler(new InjectingClasspathResourceManager(resourceRootPackage + "/login", clusterName)));
            pathHandler.addPrefixPath("/resources/", new ResourceHandler(new InjectingClasspathResourceManager(resourceRootPackage + "/resources", clusterName)));
            pathHandler.addExactPath("/logout", new LogoutHandler(pac4jConfig, "/?defaulturlafterlogout"));
            pathHandler.addExactPath("/callback", CallbackHandler.build(pac4jConfig, null, true));
            final var rootHandler = new SessionAttachmentHandler(
                    pathHandler,
                    new InMemorySessionManager("SessionManager"),
                    new SessionCookieConfig().setHttpOnly(true).setSecure(true)
            );
            addListenInterface(managementInterface, rootHandler, builder);
        }
        // Setup the api interfaces
        if (configuration.httpInterfaces != null) {
            for (var httpInterface : configuration.httpInterfaces) {
                addListenInterface(httpInterface, new ApiHttpHandler(httpInterface.alias), builder);
            }
        }
        this.server = builder.build();
    }

    private void addListenInterface(HttpInterface httpInterface, HttpHandler rootHandler, Undertow.Builder builder) {
        if (httpInterface.keystoreLocation != null && httpInterface.tlsProtocols != null && httpInterface.tlsProtocols.length > 0) {
            if (!httpInterface.keystoreLocation.exists()) {
                var parentFile = httpInterface.keystoreLocation.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
            final var keyStoreWrapper = new FileSystemKeyStoreWrapper(
                    httpInterface.keystoreLocation,
                    KeyStoreWrapper.KeyStoreType.valueOf(httpInterface.keystoreType),
                    httpInterface.getKeystorePasswordAsCharArray()
            );
            validateSelfSignedCert(keyStoreWrapper, httpInterface.getServerEntryPasswordAsCharArray());
            try {
                if (keyStoreWrapper.getKeyStore().size() == 0) {
                    // Generate self signed cert and add it to the keystore.
                    addSelfSignedCertificate(httpInterface, keyStoreWrapper);
                }
            } catch (KeyStoreException | NoSuchAlgorithmException | CertIOException | CertificateException | OperatorCreationException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
            try {
                final var keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(keyStoreWrapper.getKeyStore(), httpInterface.getServerEntryPasswordAsCharArray());
                final var keyManagers = keyManagerFactory.getKeyManagers();
                final var sslContext = SSLContext.getInstance("TLS");
                sslContext.init(keyManagers, null, null);

                httpInterface.getListenInetAddresses().forEach(c -> {
                    var listenerBuilder = new Undertow.ListenerBuilder()
                            .setType(Undertow.ListenerType.HTTPS)
                            .setOverrideSocketOptions(OptionMap.builder().set(Options.SSL_ENABLED_PROTOCOLS, Sequence.of(httpInterface.tlsProtocols)).getMap())
                            .setHost(c.getHostAddress())
                            .setPort(httpInterface.listenPort)
                            .setSslContext(sslContext)
                            .setRootHandler(rootHandler);
                    builder.addListener(listenerBuilder);
                });
            } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
        } else {
            httpInterface.getListenInetAddresses().forEach(c -> {
                var listenerBuilder = new Undertow.ListenerBuilder()
                        .setType(Undertow.ListenerType.HTTP)
                        .setHost(c.getHostAddress())
                        .setPort(httpInterface.listenPort)
                        .setRootHandler(rootHandler);
                builder.addListener(listenerBuilder);
            });
        }
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
