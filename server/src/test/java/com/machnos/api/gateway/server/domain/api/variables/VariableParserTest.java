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

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.idm.PasswordCredentials;
import com.machnos.api.gateway.server.domain.message.MockMessage;
import com.machnos.api.gateway.server.domain.transport.MockTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the <code>VariableParser</code> class.
 */
public class VariableParserTest {

    /**
     * Test getting the request <code>Message</code>.
     */
    @Test
    public void testGetRequestMessage() {
        final var requestMessage = new MockMessage();
        final var executionContext = new ExecutionContext(new MockTransport(), requestMessage, new MockMessage());
        assertEquals(requestMessage, new VariableParser().getValue("request", executionContext));
    }

    /**
     * Test parsing the request <code>Message</code>.
     */
    @Test
    public void testParseRequestMessage() {
        final var requestMessage = new MockMessage();
        final var executionContext = new ExecutionContext(new MockTransport(), requestMessage, new MockMessage());
        assertEquals(requestMessage.toString(), new VariableParser().parseAsString("${request}", executionContext));
    }

    /**
     * Test parsing a request <code>Message</code> variable.
     */
    @Test
    public void testParseRequestMessageVariable() {
        final var requestBody = "requestBody";
        final var requestMessage = new MockMessage();
        requestMessage.setBody(requestBody);
        final var executionContext = new ExecutionContext(new MockTransport(), requestMessage, new MockMessage());
        assertEquals(requestBody, new VariableParser().parseAsString("${request.body}", executionContext));
    }

    /**
     * Test getting the response <code>Message</code>.
     */
    @Test
    public void testGetResponseMessage() {
        final var responseMessage = new MockMessage();
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), responseMessage);
        assertEquals(responseMessage, new VariableParser().getValue("response", executionContext));
    }

    /**
     * Test parsing the response <code>Message</code>.
     */
    @Test
    public void testParseResponseMessage() {
        final var responseMessage = new MockMessage();
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), responseMessage);
        assertEquals(responseMessage.toString(), new VariableParser().parseAsString("${response}", executionContext));
    }

    /**
     * Test parsing a response <code>Message</code> variable.
     */
    @Test
    public void testParseResponseMessageVariable() {
        final var responseBody = "responseBody";
        final var responseMessage = new MockMessage();
        responseMessage.setBody(responseBody);
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), responseMessage);
        assertEquals(responseBody, new VariableParser().parseAsString("${response.body}", executionContext));
    }

    /**
     * Test getting the <code>Account</code>.
     */
    @Test
    public void testGetAccount() {
        final var account = new Account("test", new PasswordCredentials(new char[0]));
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.setAccount(account);
        assertEquals(account, new VariableParser().getValue("account", executionContext));
    }

    /**
     * Test parsing the <code>Account</code>.
     */
    @Test
    public void testParseAccount() {
        final var account = new Account("test", new PasswordCredentials(new char[0]));
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.setAccount(account);
        assertEquals(account.toString(), new VariableParser().parseAsString("${account}", executionContext));
    }

    /**
     * Test parsing an <code>Account</code> value.
     */
    @Test
    public void testParseAccountValue() {
        final var name = "test";
        final var account = new Account(name, new PasswordCredentials(new char[0]));
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.setAccount(account);
        assertEquals(name, new VariableParser().parseAsString("${account.username}", executionContext));
    }
}
