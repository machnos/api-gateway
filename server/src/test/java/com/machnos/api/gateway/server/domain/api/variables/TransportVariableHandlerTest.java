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

import com.machnos.api.gateway.server.domain.transport.MockHttpTransport;
import com.machnos.api.gateway.server.domain.transport.MockSecurity;
import com.machnos.api.gateway.server.domain.transport.MockTransport;
import com.machnos.api.gateway.server.domain.transport.Transport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for the <code>TransportVariableHandler</code> class.
 */
public class TransportVariableHandlerTest extends AbstractVariableHandlerTest<TransportVariableHandler, Transport> {

    @Override
    protected TransportVariableHandler getHandlerInstance() {
        return new TransportVariableHandler();
    }

    @Override
    protected MockTransport getObjectToHandle() {
        return new MockTransport();
    }

    /**
     * Test getting the <code>HttpTransport</code> instance.
     */
    @Test
    public void testGetHttpTransport() {
        final var variableHandler = getHandlerInstance();
        final var transport = getObjectToHandle();
        final var httpTransport = new MockHttpTransport();
        transport.setHttpTransport(httpTransport);
        transport.setHttp(true);
        assertEquals(httpTransport, variableHandler.getValue(TransportVariableHandler.PREFIX_HTTP, transport));
        transport.setHttp(false);
        assertNull(variableHandler.getValue(TransportVariableHandler.PREFIX_HTTP, transport));
    }

    /**
     * Test getting the <code>Security</code> instance.
     */
    @Test
    public void testGetSecurity() {
        final var variableHandler = getHandlerInstance();
        final var transport = getObjectToHandle();
        final var security = new MockSecurity();
        transport.setSecurity(security);
        assertEquals(security, variableHandler.getValue(TransportVariableHandler.PREFIX_SECURITY, transport));
    }

    /**
     * Test getting the interface alias value.
     */
    @Test
    public void testGetInterfaceAlias() {
        final var variableHandler = getHandlerInstance();
        final var transport = getObjectToHandle();
        final var alias = "internet-facing";
        transport.setInterfaceAlias(alias);
        assertEquals(alias, variableHandler.getValue(TransportVariableHandler.INTERFACE_ALIAS, transport));
    }

    /**
     * Test getting the is http flag.
     */
    @Test
    public void testGetIsHttp() {
        final var variableHandler = getHandlerInstance();
        final var transport = getObjectToHandle();
        transport.setHttp(false);
        assertEquals(false, variableHandler.getValue(TransportVariableHandler.IS_HTTP, transport));
        transport.setHttp(true);
        assertEquals(true, variableHandler.getValue(TransportVariableHandler.IS_HTTP, transport));
    }

    /**
     * Test getting the is secure flag.
     */
    @Test
    public void testGetIsSecure() {
        final var variableHandler = getHandlerInstance();
        final var transport = getObjectToHandle();
        transport.setSecure(false);
        assertEquals(false, variableHandler.getValue(TransportVariableHandler.IS_SECURE, transport));
        transport.setSecure(true);
        assertEquals(true, variableHandler.getValue(TransportVariableHandler.IS_SECURE, transport));
    }
}
