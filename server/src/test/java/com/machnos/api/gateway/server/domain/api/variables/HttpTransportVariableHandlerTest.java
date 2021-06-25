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

import com.machnos.api.gateway.server.domain.transport.HttpTransport;
import com.machnos.api.gateway.server.domain.transport.MockHttpTransport;
import com.machnos.api.gateway.server.domain.transport.MockRequestURL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test class for the <code>HttpTransportVariableHandler</code> class.
 */
public class HttpTransportVariableHandlerTest extends AbstractVariableHandlerTest<HttpTransportVariableHandler, HttpTransport> {

    @Override
    protected HttpTransportVariableHandler getHandlerInstance() {
        return new HttpTransportVariableHandler();
    }

    @Override
    protected MockHttpTransport getObjectToHandle() {
        return new MockHttpTransport();
    }

    /**
     * Test getting the http 0.9 value.
     */
    @Test
    public void testGetHttp09() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        httpTransport.setHttp09(false);
        assertEquals(false, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_09, httpTransport));
        httpTransport.setHttp09(true);
        assertEquals(true, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_09, httpTransport));
    }

    /**
     * Test getting the http 1.0 value.
     */
    @Test
    public void testGetHttp10() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        httpTransport.setHttp10(false);
        assertEquals(false, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_10, httpTransport));
        httpTransport.setHttp10(true);
        assertEquals(true, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_10, httpTransport));
    }

    /**
     * Test getting the http 1.1 value.
     */
    @Test
    public void testGetHttp11() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        httpTransport.setHttp11(false);
        assertEquals(false, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_11, httpTransport));
        httpTransport.setHttp11(true);
        assertEquals(true, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_11, httpTransport));
    }

    /**
     * Test getting the http 2.0 value.
     */
    @Test
    public void testGetHttp20() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        httpTransport.setHttp20(false);
        assertEquals(false, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_20, httpTransport));
        httpTransport.setHttp20(true);
        assertEquals(true, variableHandler.getValue(HttpTransportVariableHandler.IS_HTTP_20, httpTransport));
    }

    /**
     * Test getting the request method value.
     */
    @Test
    public void testGetRequestMethod() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        final var method = "POST";
        httpTransport.setRequestMethod(method);
        assertEquals(method, variableHandler.getValue(HttpTransportVariableHandler.REQUEST_METHOD, httpTransport));
    }

    /**
     * Test getting the requestUrl value.
     */
    @Test
    public void testGetRequestUrl() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        final var requestUrl = new MockRequestURL();
        httpTransport.setRequestURL(requestUrl);
        assertSame(requestUrl, variableHandler.getValue(HttpTransportVariableHandler.PREFIX_REQUEST_URL, httpTransport));
    }

    /**
     * Test getting the response status code value.
     */
    @Test
    public void testGetResponseStatusCode() {
        final var variableHandler = getHandlerInstance();
        final var httpTransport = getObjectToHandle();
        final var statusCode = 200;
        httpTransport.setResponseStatusCode(statusCode);
        assertEquals(statusCode, variableHandler.getValue(HttpTransportVariableHandler.RESPONSE_STATUS_CODE, httpTransport));
    }
}
