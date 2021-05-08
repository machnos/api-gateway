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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the <code>Server</code> class.
 */
public class ServerTest {

    /**
     * Test starting and stopping the server.
     */
    @Test
    public void testStartStop() {
        var configuration = new Configuration();
        configuration.clusterName = "test-cluster";
        var managementInterface = new HttpInterface();
        var server = new Server(configuration);
        // Test the initial running state
        assertFalse(server.isRunning());

        // Stopping a server that is not running should have no effect.
        server.stop();
        assertFalse(server.isRunning());

        // Test starting the server
        try (final var socket = new ServerSocket(managementInterface.listenPort, 50, InetAddress.getByName(managementInterface.listenInterface))) {
            // Server socket can be opened, testcase can proceed.
        }
        catch (IOException e) {
            fail(String.format("Port '%d' cannot be opened on interface '%s'.", managementInterface.listenPort, managementInterface.listenInterface), e);
        }
        server.start();
        assertTrue(server.isRunning());
        // It should not be possible to open a socket on the same interface + port as the server is running on.
        assertThrows(IOException.class, () -> {
            new ServerSocket(managementInterface.listenPort, 50, InetAddress.getByName(managementInterface.listenInterface));
        });

        // Starting the server again should have no effect.
        server.start();
        assertTrue(server.isRunning());

        // And finally stopping the server should result in the initial state.
        server.stop();
        assertFalse(server.isRunning());
        // Opening a socket on the same interface + port should succeed now.
        try (final var socket = new ServerSocket(managementInterface.listenPort, 50, InetAddress.getByName(managementInterface.listenInterface))) {
            // Server socket can be opened, testcase can proceed.
        }
        catch (IOException e) {
            fail(String.format("Port '%d' cannot be opened on interface '%s'.", managementInterface.listenPort, managementInterface.listenInterface), e);
        }
    }
}