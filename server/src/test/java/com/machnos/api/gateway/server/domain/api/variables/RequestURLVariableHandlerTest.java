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

import com.machnos.api.gateway.server.domain.transport.MockRequestURL;
import com.machnos.api.gateway.server.domain.transport.RequestURL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the <code>RequestURLVariableHandler</code> class.
 */
public class RequestURLVariableHandlerTest extends AbstractVariableHandlerTest<RequestURLVariableHandler, RequestURL> {

    @Override
    protected RequestURLVariableHandler getHandlerInstance() {
        return new RequestURLVariableHandler();
    }

    @Override
    protected MockRequestURL getObjectToHandle() {
        return new MockRequestURL();
    }

    /**
     * Test getting the scheme value.
     */
    @Test
    public void testGetScheme() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final var scheme = "https";
        requestURL.setScheme(scheme);
        assertEquals(scheme, variableHandler.getValue(RequestURLVariableHandler.SCHEME, requestURL));
    }

    /**
     * Test getting the host value.
     */
    @Test
    public void testGetHost() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final var host = "127.0.0.1";
        requestURL.setHost(host);
        assertEquals(host, variableHandler.getValue(RequestURLVariableHandler.HOST, requestURL));
    }

    /**
     * Test getting the port value.
     */
    @Test
    public void testGetPort() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final var port = 8443;
        requestURL.setPort(port);
        assertEquals(port, variableHandler.getValue(RequestURLVariableHandler.PORT, requestURL));
    }

    /**
     * Test getting the path value.
     */
    @Test
    public void testGetPath() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final var path = "/my/api/";
        requestURL.setPath(path);
        assertEquals(path, variableHandler.getValue(RequestURLVariableHandler.PATH, requestURL));
    }

    /**
     * Test getting the fragment value.
     */
    @Test
    public void testGetFragment() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final var fragment = "#main";
        requestURL.setFragment(fragment);
        assertEquals(fragment, variableHandler.getValue(RequestURLVariableHandler.FRAGMENT, requestURL));
    }

    /**
     * Test getting the query value.
     */
    @Test
    public void testGetQuery() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final String query = "?key=1&other=not";
        requestURL.setQuery(query);
        assertEquals(query, variableHandler.getValue(RequestURLVariableHandler.QUERY, requestURL));
    }

    /**
     * Test getting a query parameter value.
     */
    @Test
    public void testGetQueryParameter() {
        final var variableHandler = getHandlerInstance();
        final var requestURL = getObjectToHandle();
        final String queryParameterName = "key";
        final String queryParameterValue = "1";
        requestURL.setQueryParameter(queryParameterName, queryParameterValue);
        assertEquals(queryParameterValue, variableHandler.getValue(RequestURLVariableHandler.PREFIX_QUERY_PARAMETER + queryParameterName, requestURL));
    }
}
