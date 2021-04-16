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

import org.junit.jupiter.api.Test;

import java.net.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * Test class for the <code>HttpInterface</code> class
 */
public class HttpInterfaceTest {

    @Test
    public void testGetListenInetAddressesOnNetworkInterfaces() {
        try {
            var networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                var networkInterface = networkInterfaces.nextElement();
                final var httpInterface = new HttpInterface();
                httpInterface.listenInterface = networkInterface.getName();

                var inetAddresses = httpInterface.getListenInetAddresses();
                var actualInetAddresses = networkInterface.inetAddresses().collect(Collectors.toList());
                assertIterableEquals(actualInetAddresses, inetAddresses);

                // Test for ipv4 only addresses when available.
                var actualIpv4Addresses = actualInetAddresses.stream().filter(p -> p instanceof Inet4Address).collect(Collectors.toList());
                if (actualIpv4Addresses.size() > 0) {
                    httpInterface.listenInterface = networkInterface.getName() + HttpInterface.IPV4_SUFFIX;
                    inetAddresses = httpInterface.getListenInetAddresses();
                    assertIterableEquals(actualIpv4Addresses, inetAddresses);
                }

                // Test for ipv6 only addresses when available.
                var actualIpv6Addresses = actualInetAddresses.stream().filter(p -> p instanceof Inet6Address).collect(Collectors.toList());
                if (actualIpv6Addresses.size() > 0) {
                    httpInterface.listenInterface = networkInterface.getName() + HttpInterface.IPV6_SUFFIX;
                    inetAddresses = httpInterface.getListenInetAddresses();
                    assertIterableEquals(actualIpv6Addresses, inetAddresses);
                }
            }
        } catch (SocketException e) {
            // NO-OP no interfaces to test.
        }
    }

    @Test
    public void testGetListenInetAddressesOnHostNames() {
        try {
            var localAddress = InetAddress.getLocalHost();
            final var httpInterface = new HttpInterface();

            httpInterface.listenInterface = localAddress.getHostAddress();
            assertEquals(1, httpInterface.getListenInetAddresses().size());
            assertEquals(localAddress, httpInterface.getListenInetAddresses().get(0));

            httpInterface.listenInterface = localAddress.getHostName();
            assertEquals(1, httpInterface.getListenInetAddresses().size());
            assertEquals(localAddress, httpInterface.getListenInetAddresses().get(0));

        } catch (UnknownHostException e) {
            // NO-OP no interfaces to test.
        }
    }
}
