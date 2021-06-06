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
 * Class representing a message that is based on an http request or response.
 */
public interface HttpMessage extends Message {

    /**
     * The http message type.
     */
    enum Type {
        /**
         * Enum representing an http request.
         */
        REQUEST,
        /**
         * Enum representing an http response.
         */
        RESPONSE;

        /**
         * Determine if this <code>Type</code> is an http request.
         *
         * @return <code>true</code> when this <code>Type</code> is an http request, <code>false</code> otherwise.
         */
        public boolean isRequest() {
            return Type.REQUEST.equals(this);
        }

        /**
         * Determine if this <code>Type</code> is an http response.
         *
         * @return <code>true</code> when this <code>Type</code> is an http response, <code>false</code> otherwise.
         */
        public boolean isResponse() {
            return Type.RESPONSE.equals(this);
        }
    }


    /**
     * The http status code for unauthorized request.
     */
    int STATUS_CODE_UNAUTHORIZED = 401;

    @Override
    default boolean isHttp() { return true; }

    @Override
    default HttpMessage getHttpMessage() { return this; }

}
