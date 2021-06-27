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

import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.message.MockHeaders;
import com.machnos.api.gateway.server.domain.message.MockMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test class for the <code>MessageVariableHandler</code> class.
 */
public class MessageVariableHandlerTest extends AbstractVariableHandlerTest<MessageVariableHandler, Message> {

    @Override
    protected MessageVariableHandler getHandlerInstance() {
        return new MessageVariableHandler();
    }

    @Override
    protected MockMessage getObjectToHandle() {
        return new MockMessage();
    }

    /**
     * Test getting the body value.
     */
    @Test
    public void testGetBody() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var body = "This is just plain text";
        message.setBody(body);
        assertEquals(body, variableHandler.getValue(MessageVariableHandler.BODY, message));
    }

    /**
     * Test getting the headers value.
     */
    @Test
    public void testGetHeaders() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        assertSame(headers, variableHandler.getValue(MessageVariableHandler.HEADERS, message));
    }

    /**
     * Test getting the headers size value.
     */
    @Test
    public void testGetHeadersSize() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        assertEquals(0, (Integer) variableHandler.getValue(MessageVariableHandler.HEADERS_SIZE, message));
        headers.set("name", "value");
        assertEquals(1, (Integer) variableHandler.getValue(MessageVariableHandler.HEADERS_SIZE, message));
    }

    /**
     * Test getting the header names value.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetHeadersNames() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        var names = (List<String>)variableHandler.getValue(MessageVariableHandler.HEADERS_NAMES, message);
        assertEquals(0, names.size());

        headers.set("name", "value");
        names = variableHandler.getValue(MessageVariableHandler.HEADERS_NAMES, message);
        assertEquals(1, names.size());
    }

    /**
     * Test getting a header value.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testGetHeaderValues() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        headers.set("name", "value");
        final var values = (List<String>)variableHandler.getValue(MessageVariableHandler.PREFIX_HEADER + "name", message);
        assertEquals(1, values.size());
    }

    /**
     * Test getting the size of a header value.
     */
    @Test
    public void testGetHeaderValueSize() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        final var values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        headers.set("name", values);
        final var size = variableHandler.getValue(MessageVariableHandler.PREFIX_HEADER + "name" + AbstractVariableHandler.SUFFIX_COLLECTION_SIZE, message);
        assertEquals(3, size);
    }

    /**
     * Test getting the nth value of a header.
     */
    @Test
    public void testGetHeaderValueAtIndex() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        final var headers = new MockHeaders();
        message.setHeaders(headers);
        final var values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        values.add("value3");
        headers.set("name", values);
        assertEquals("value2", variableHandler.getValue(MessageVariableHandler.PREFIX_HEADER + "name[1]", message));
    }

    /**
     * Test getting is http value.
     */
    @Test
    public void testIsHttp() {
        final var variableHandler = getHandlerInstance();
        final var message = getObjectToHandle();
        message.setHttp(false);
        assertEquals(false, variableHandler.getValue(MessageVariableHandler.IS_HTTP, message));
        message.setHttp(true);
        assertEquals(true, variableHandler.getValue(MessageVariableHandler.IS_HTTP, message));
    }

}
