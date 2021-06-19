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

package com.machnos.api.gateway.server.domain.message;

import java.util.List;

/**
 * The headers that come with a <code>Message</code>.
 */
public interface Headers {

    /**
     * The http Authorization header name.
     */
    String HTTP_AUTHORIZATION = "Authorization";
    /**
     * The http Content-Type header name.
     */
    String HTTP_CONTENT_TYPE = "Content-Type";
    /**
     * The http WWW-Authenticate header name.
     */
    String HTTP_WWW_AUTHENTICATE = "WWW-Authenticate";

    /**
     * Boolean indicating the <code>Headers</code> contain a header with a given name.
     *
     * @param headerName The name of the header to check the presence for.
     * @return <code>true</code> if a header with the given headerName exists, <code>false</code> otherwise.
     */
    boolean contains(String headerName);

    /**
     * Gives the nth value of a header with the given headerName.
     *
     * @param headerName The name of the header that contains the value.
     * @param index The index of the header value.
     * @return The nth value of a header with the given headerName, or <code>null</code> when no such header exists.
     */
    String getNth(String headerName, int index);

    /**
     * Gives the values of a header with the given headerName.
     *
     * @param headerName The name of the header that contains the value.
     * @return The values of a header with a given name, or <code>null</code> when nu such header exists.
     */
    List<String> get(String headerName);

    /**
     * Sets a header value. When a header with the given headerName already exists, it's value will be overwritten.
     *
     * @param headerName The name of the header value to set.
     * @param headerValue The value the header should have.
     */
    void set(String headerName, String headerValue);

    /**
     * Sets multiple header values. When a header with the given headerName already exists, it's values will be overwritten.
     *
     * @param headerName The name of the header values to set.
     * @param headerValues The values the header should have.
     */
    void set(String headerName, List<String> headerValues);

    /**
     * Gives a list of available header names.
     *
     * @return A (possible empty) list of header names.
     */
    List<String> getHeaderNames();

    /**
     * Gives the number of entries that are stored with this <code>Headers</code> instance.
     *
     * @return The number of entries.
     */
    int getSize();
}
