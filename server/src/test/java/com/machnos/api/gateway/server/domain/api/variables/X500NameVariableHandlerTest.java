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

import com.machnos.api.gateway.server.domain.transport.x509.X500Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the <code>X500NameVariableHandler</code> class.
 */
public class X500NameVariableHandlerTest extends AbstractVariableHandlerTest<X500NameVariableHandler, X500Name> {

    /**
     * The common name value to test.
     */
    private final String cn = "TestCase";
    /**
     * The country value to test.
     */
    private final String c = "NL";
    /**
     * The organization value to test.
     */
    private final String o = "Machnos";
    /**
     * The organizational unit value to test.
     */
    private final String ou = "Test department";
    /**
     * The locality value to test.
     */
    private final String l = "Mountain View";
    /**
     * The state/province value to test.
     */
    private final String st = "Overijssel";
    /**
     * The street value to test.
     */
    private final String street = "Mainstreet";
    /**
     * The domain component value to test.
     */
    private final String dc = "directory";
    /**
     * The email address value to test.
     */
    private final String e = "noreply@machnos.com";
    /**
     * The user id value to test.
     */
    private final String uid = "ab123";
    /**
     * The distinguished name value to test.
     */
    private final String dn = String.format("CN=%s,C=%s,O=%s,OU=%s,L=%s,ST=%s,STREET=%s,DC=%s,E=%s,UID=%s",
            this.cn, this.c, this.o, this.ou, this.l, this.st, this.street, this.dc, this.e, this.uid);

    @Override
    protected X500NameVariableHandler getHandlerInstance() {
        return new X500NameVariableHandler();
    }

    @Override
    protected X500Name getObjectToHandle() {
        return new X500Name(new org.bouncycastle.asn1.x500.X500Name(this.dn));
    }

    /**
     * Test getting the distinguished name value.
     */
    @Test
    public void testGetDN() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.dn, variableHandler.getValue(X500NameVariableHandler.DISTINGUISHED_NAME, x500Name));
    }

    /**
     * Test getting the common name value.
     */
    @Test
    public void testGetCN() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.cn, variableHandler.getValue(X500NameVariableHandler.COMMON_NAME, x500Name));
    }

    /**
     * Test getting the country value.
     */
    @Test
    public void testGetC() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.c, variableHandler.getValue(X500NameVariableHandler.COUNTRY, x500Name));
    }

    /**
     * Test getting the organization value.
     */
    @Test
    public void testGetO() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.o, variableHandler.getValue(X500NameVariableHandler.ORGANIZATION, x500Name));
    }

    /**
     * Test getting the organizational unit value.
     */
    @Test
    public void testGetOU() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.ou, variableHandler.getValue(X500NameVariableHandler.ORGANIZATIONAL_UNIT, x500Name));
    }

    /**
     * Test getting the locality value.
     */
    @Test
    public void testGetL() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.l, variableHandler.getValue(X500NameVariableHandler.LOCALITY, x500Name));
    }

    /**
     * Test getting the state value.
     */
    @Test
    public void testGetST() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.st, variableHandler.getValue(X500NameVariableHandler.STATE, x500Name));
    }

    /**
     * Test getting the street value.
     */
    @Test
    public void testGetStreet() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.street, variableHandler.getValue(X500NameVariableHandler.STREET, x500Name));
    }

    /**
     * Test getting the domain component value.
     */
    @Test
    public void testGetDC() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.dc, variableHandler.getValue(X500NameVariableHandler.DOMAIN_COMPONENT, x500Name));
    }

    /**
     * Test getting the email address value.
     */
    @Test
    public void testGetEmailAddress() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.e, variableHandler.getValue(X500NameVariableHandler.EMAIL_ADDRESS, x500Name));
    }

    /**
     * Test getting the user id value.
     */
    @Test
    public void testGetUID() {
        final var variableHandler = getHandlerInstance();
        final var x500Name = getObjectToHandle();
        assertEquals(this.uid, variableHandler.getValue(X500NameVariableHandler.USER_ID, x500Name));
    }
}
