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

/**
 * Mock <code>Headers</code> implementation with setter methods for all properties.
 */
public class MockMessage implements Message {

    /**
     * The <code>Headers</code>.
     */
    private Headers headers;
    /**
     * The body of the message.
     */
    private String body;

    /**
     * Boolean indicating this <code>MockMessage</code> is simulating an http message.
     */
    private boolean http;

    @Override
    public Headers getHeaders() {
        return this.headers;
    }

    /**
     * Sets the <code>Headers</code>.
     *
     * @param headers The <code>Headers</code> to set.
     */
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @Override
    public String getBody() {
        return this.body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean isHttp() {
        return this.http;
    }

    /**
     * Set the http flag.
     *
     * @param http <code>true</code> when this <code>MockMessage</code> should simulate an http message, <code>false</code> otherwise.
     */
    public void setHttp(boolean http) {
        this.http = http;
    }
}
