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
 * Class representing the communication with a client via the HTTP protocol.
 */
public interface HttpTransport extends Transport {

    @Override
    default boolean isHttp() { return true; }

    @Override
    default HttpTransport getHttpTransport() {return this;}

    /**
     * Determine if version 0.9 of the HTTP protocol is used as a transport mechanism.
     *
     * @return <code>true</code> when version 0.9 of the HTTP protocol is used <code>false</code> otheriwse.
     */
    boolean isHttp09();

    /**
     * Determine if version 1.0 of the HTTP protocol is used as a transport mechanism.
     *
     * @return <code>true</code> when version 1.0 of the HTTP protocol is used <code>false</code> otheriwse.
     */
    boolean isHttp10();

    /**
     * Determine if version 1.1 of the HTTP protocol is used as a transport mechanism.
     *
     * @return <code>true</code> when version 1.1 of the HTTP protocol is used <code>false</code> otheriwse.
     */
    boolean isHttp11();

    /**
     * Determine if version 2.0 of the HTTP protocol is used as a transport mechanism.
     *
     * @return <code>true</code> when version 2.0 of the HTTP protocol is used <code>false</code> otheriwse.
     */
    boolean isHttp20();

    /**
     * Gives the request method that was used when sending the request.
     *
     * @return The request method.
     */
    String getRequestMethod();

    /**
     * Gives the <code>RequestURL</code> to which the request message was sent.
     *
     * @return The <code>RequestURL</code> to which the request message was sent.
     */
    RequestURL getRequestURL();

    /**
     * Gives the http status code that is, or will be send to the client.
     *
     * @return The http status code.
     */
    int getResponseStatusCode();

    /**
     * Sets the http status code that will be send to the client.
     *
     * @param statusCode The http status code.
     */
    void setResponseStatusCode(int statusCode);

}
