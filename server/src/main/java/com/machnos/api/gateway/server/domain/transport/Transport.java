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

package com.machnos.api.gateway.server.domain.transport;

/**
 * Class representing the communication with a client.
 */
public interface Transport {

    /**
     * Gives the alias of the interface that was used to communicate with the remote application.
     *
     * @return The alias of the interface.
     * @see com.machnos.api.gateway.server.configuration.HttpInterface#alias
     */
    String getInterfaceAlias();

    /**
     * Determines if the transport is secure.
     *
     * @return <code>true</code> if transport security is used, <code>false</code> otherwise.
     * @see #getSecurity()
     */
    boolean isSecure();

    /**
     * Gives the <code>Security</code> that is used at transport level.
     *
     * @return The <code>Security</code>, or <code>null</code> when no security is applied at transport level.
     * @see #isSecure()
     */
    Security getSecurity();

    /**
     * Determines if the HTTP protocol is used as a transport mechanism.
     *
     * @return <code>true</code> when the HTTP protocol is used, <code>false</code> otherwise.
     * @see #getHttpTransport()
     */
    boolean isHttp();

    /**
     * Gives the <code>HttpTransport</code> if the HTTP protocol is used as a transport mechanism.
     *
     * @return The <code>HttpTransport</code>, or <code>null</code> when the HTTP protocol was not used.
     * @see #isHttp()
     */
    HttpTransport getHttpTransport();

}
