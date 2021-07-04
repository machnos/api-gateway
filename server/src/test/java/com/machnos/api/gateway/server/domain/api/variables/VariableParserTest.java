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
import com.machnos.api.gateway.server.domain.api.MockApi;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.idm.PasswordCredentials;
import com.machnos.api.gateway.server.domain.message.MockMessage;
import com.machnos.api.gateway.server.domain.transport.MockRequestURL;
import com.machnos.api.gateway.server.domain.transport.MockSecurity;
import com.machnos.api.gateway.server.domain.transport.MockTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    /**
     * Test getting the <code>Transport</code>.
     */
    @Test
    public void testGetTransport() {
        final var transport = new MockTransport();
        final var executionContext = new ExecutionContext(transport, new MockMessage(), new MockMessage());
        assertEquals(transport, new VariableParser().getValue("transport", executionContext));
    }

    /**
     * Test parsing the <code>Transport</code>.
     */
    @Test
    public void testParseTransport() {
        final var transport = new MockTransport();
        final var executionContext = new ExecutionContext(transport, new MockMessage(), new MockMessage());
        assertEquals(transport.toString(), new VariableParser().parseAsString("${transport}", executionContext));
    }

    /**
     * Test parsing a <code>Transport</code> value.
     */
    @Test
    public void testParseTransportValue() {
        final var securityProtocol = "TLSv1.3";
        final var transport = new MockTransport();
        final var security = new MockSecurity();
        transport.setSecurity(security);
        security.setProtocol(securityProtocol);
        final var executionContext = new ExecutionContext(transport, new MockMessage(), new MockMessage());
        assertEquals(securityProtocol, new VariableParser().parseAsString("${transport.security.protocol}", executionContext));
    }

    /**
     * Test getting the <code>Api</code>.
     */
    @Test
    public void testGetApi() {
        final var api = new MockApi() {
            @Override
            public void handleRequest(ExecutionContext executionContext) {
                assertEquals(this, executionContext.getVariable("api"));
            }
        };
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.executeApi(api);
    }

    /**
     * Test parsing the <code>Api</code>.
     */
    @Test
    public void testParseApi() {
        final var api = new MockApi() {
            @Override
            public void handleRequest(ExecutionContext executionContext) {
                assertEquals(this.toString(), executionContext.parseVariableAsString("${api}"));
            }
        };
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.executeApi(api);
    }

    /**
     * Test parsing an <code>Account</code> value.
     */
    @Test
    public void testParseApiValue() {
        final var name = "test";
        final var api = new MockApi() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void handleRequest(ExecutionContext executionContext) {
                assertEquals(name, executionContext.parseVariableAsString("${api.name}"));
            }
        };
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.executeApi(api);
    }

    /**
     * Test getting a custom value.
     */
    @Test
    public void testGetCustomValue() {
        final var name = "custom.variable";
        final var value = "custom.value";
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.getVariables().startScope();
        executionContext.getVariables().setVariable(name, value);
        assertEquals(value, new VariableParser().getValue("custom.variable", executionContext));
    }

    /**
     * Test parsing a custom value.
     */
    @Test
    public void testParseCustomValue() {
        final var name = "custom.variable";
        final var value = "custom.value";
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.getVariables().startScope();
        executionContext.getVariables().setVariable(name, value);
        assertEquals(value, new VariableParser().parseAsString("${custom.variable}", executionContext));
    }

    /**
     * Test parsing a custom object value.
     */
    @Test
    public void testParseCustomObjectValue() {
        final var host = "www.machnos.com";
        final var name = "custom.url";
        final var value = new MockRequestURL()
                .setScheme("https")
                .setHost(host)
                .setPort(443)
                .setPath("/");
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        executionContext.getVariables().startScope();
        executionContext.getVariables().setVariable(name, value);
        assertEquals(host, new VariableParser().parseAsString("${custom.url.host}", executionContext));
    }

    /**
     * Test getting a value using a <code>null</code> value name.
     */
    @Test
    public void testGetNullVariableName() {
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        assertNull(new VariableParser().getValue(null, executionContext));
    }

    /**
     * Test parsing a value using a <code>null</code> value name.
     */
    @Test
    public void testParseNullVariableName() {
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        assertNull(new VariableParser().parseAsString(null, executionContext));
    }

    /**
     * Test getting a value using an empty variable name.
     */
    @Test
    public void testGetEmptyVariableName() {
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        assertNull(new VariableParser().getValue("", executionContext));
    }

    /**
     * Test parsing a value using an empty input variable.
     */
    @Test
    public void testParseEmptyVariableName() {
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        assertEquals("", new VariableParser().parseAsString("${}", executionContext));
    }

    /**
     * Test parsing a value using an invalid input variable.
     */
    @Test
    public void testParseInvalidVariableName() {
        final var invalidName = "${";
        final var executionContext = new ExecutionContext(new MockTransport(), new MockMessage(), new MockMessage());
        assertEquals(invalidName, new VariableParser().parseAsString(invalidName, executionContext));
    }
}
