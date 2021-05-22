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

package com.machnos.api.gateway.server.configuration;

import com.machnos.api.gateway.server.domain.MachnosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing an http interface.
 */
public class HttpInterface {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    public static final String IPV4_SUFFIX = ":ipv4";
    public static final String IPV6_SUFFIX = ":ipv6";

    public String alias;
    public String listenInterface = "127.0.0.1";
    public int listenPort = 8443;
    public String[] tlsProtocols = new String[] { "TLSv1.2", "TLSv1.3" };
    public File keystoreLocation = new File(Configuration.CONFIG_DIRECTORY, "keystore.jks");
    public String keystorePassword;
    public String keystoreType = "JKS";
    public String serverEntryPassword;

    /**
     * Returns the keystore password as char array.
     *
     * @return The keystore password as char array, or an empty char array when the password is not set.
     */
    public char[] getKeystorePasswordAsCharArray() {
        if (this.keystorePassword != null) {
            return this.keystorePassword.toCharArray();
        }
        return new char[0];
    }

    /**
     * Returns the password for the server private key & certificate in the keystore.
     *
     * @return The password to retrieve the server private key & certificate from the keystore, or an empty char
     * array when the password is not set.
     */
    public char[] getServerEntryPasswordAsCharArray() {
        if (this.serverEntryPassword != null) {
            return this.serverEntryPassword.toCharArray();
        }
        return new char[0];
    }

    /**
     * Gives the <code>InetAddress</code>es this configuration should be listening on.
     *
     * First the {@link #listenInterface} is checked against the names of all interfaces known to the host. If there is
     * a match, all <code>InetAddress</code>es that belong to that interface are returned. If no match is made on the
     * interface names the value of {@link #listenInterface} is matched against a list of hostnames or ip addresses.
     * If there is a match the corresponding <code>InetAddress</code>es will be returned. If no match can be made a
     * <code>MachnosException</code> with code {@link MachnosException#INVALID_INTERFACE} will be returned.
     *
     * When the {@link #listenInterface} setting is provided with an interface name or a hostname, the result can be
     * limited with ipv4 or ipv6 addresses by appending the value with {@link #IPV4_SUFFIX} or {@link #IPV6_SUFFIX}.
     *
     * @return The <code>InetAddress</code>es this configuration should be listening on
     */
    public List<InetAddress> getListenInetAddresses() {
        var interfaceToTest = this.listenInterface;
        var limitToIpv4 = false;
        var limitToIpv6 = false;
        if (this.listenInterface.endsWith(IPV4_SUFFIX)) {
            interfaceToTest = this.listenInterface.substring(0, this.listenInterface.lastIndexOf(IPV4_SUFFIX));
            limitToIpv4 = true;
        } else if (this.listenInterface.endsWith(IPV6_SUFFIX)) {
            interfaceToTest = this.listenInterface.substring(0, this.listenInterface.lastIndexOf(IPV6_SUFFIX));
            limitToIpv6 = true;
        }
        List<InetAddress> result = getInetAddressesForInterfaceName(interfaceToTest);
        if (result == null) {
            result = getInetAddressesForHostname(interfaceToTest);
        }
        if (result == null) {
            throw new MachnosException(MachnosException.INVALID_INTERFACE);
        }
        if (limitToIpv4) {
            result = result.stream().filter(p -> p instanceof Inet4Address).collect(Collectors.toList());
        } else if (limitToIpv6) {
            result = result.stream().filter(p -> p instanceof Inet6Address).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Gives a list of <code>InetAddress</code>es for a given network interface.
     *
     * @param interfaceName The name of the network interface.
     * @return The <code>InetAddress</code>es of the give network interface, or <code>null</code> if no such network
     * interface exists.
     */
    private List<InetAddress> getInetAddressesForInterfaceName(String interfaceName) {
        try {
            final var networkInterface = NetworkInterface.getByName(interfaceName);
            if (networkInterface != null) {
                return networkInterface.getInterfaceAddresses().stream().map(InterfaceAddress::getAddress).collect(Collectors.toList());
            }
        } catch (SocketException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Listen interface '" + interfaceName + "' seems not to be a network interface.", e);
            }
        }
        return null;
    }

    /**
     * Gives a list of <code>InetAddress</code>es for a given hostname or ip address.
     *
     * @param hostname The name of this host.
     * @return The <code>InetAddress</code>es of the give host.
     */
    private List<InetAddress> getInetAddressesForHostname(String hostname) {
        try {
            return Arrays.asList(InetAddress.getAllByName(hostname));
        } catch (UnknownHostException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Listen interface '" + hostname + "' seems not to be the hosts internet address or name.", e);
            }
        }
        return null;
    }
}
