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

import com.machnos.api.gateway.server.domain.transport.Transport;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>Transport</code> instance.
 */
public class TransportVariableHandler extends AbstractVariableHandler<Transport> {

    /**
     * The prefix for the <code>HttpTransport</code> object.
     */
    public static final String PREFIX_HTTP = "http";
    /**
     * The length of the prefix for the <code>HttpTransport</code> object.
     */
    public static final int PREFIX_HTTP_LENGTH = PREFIX_HTTP.length();
    /**
     * The prefix for the <code>Security</code> object.
     */
    public static final String PREFIX_SECURITY = "security";
    /**
     * The length of the prefix for the <code>Security</code> object.
     */
    public static final int PREFIX_SECURITY_LENGTH = PREFIX_SECURITY.length();
    /**
     * The interface alias variable.
     */
    public static final String INTERFACE_ALIAS = "interfacealias";
    /**
     * The is http variable.
     */
    public static final String IS_HTTP = "ishttp";
    /**
     * The is secure variable.
     */
    public static final String IS_SECURE = "issecure";

    /**
     * The <code>VariableHandler</code> that can handle values for the <code>HttpTransport</code> objects.
     */
    private final HttpTransportVariableHandler httpTransportVariableHandler = new HttpTransportVariableHandler();
    /**
     * The <code>VariableHandler</code> that can handle values for the <code>Security</code> objects.
     */
    private final SecurityVariableHandler securityVariableHandler = new SecurityVariableHandler();

    @Override
    public Object getValue(String variable, Transport transport) {
        if (variable == null || transport == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return transport;
        } else if (variable.startsWith(PREFIX_HTTP)) {
            if (!transport.isHttp()) {
                return null;
            }
            return this.httpTransportVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_HTTP_LENGTH), transport.getHttpTransport());
        } else if (variable.startsWith(PREFIX_SECURITY)) {
            return this.securityVariableHandler.getValue(determineChildObjectVariableName(variable, PREFIX_SECURITY_LENGTH), transport.getSecurity());
        } else if (INTERFACE_ALIAS.equals(variable)) {
            return transport.getInterfaceAlias();
        } else if (IS_HTTP.equals(variable)) {
            return transport.isHttp();
        } else if (IS_SECURE.equals(variable)) {
            return transport.isSecure();
        }
        return null;
    }
}
