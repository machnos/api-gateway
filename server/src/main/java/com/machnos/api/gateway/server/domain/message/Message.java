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
 * Class representing a message that can be send to, or received from a client.
 */
public interface Message {

    /**
     * Gives the <code>Headers</code> that belong to the <code>Message</code>.
     *
     * @return The <code>Headers</code> of the <code>Message</code>.
     */
    Headers getHeaders();

    /**
     * Gives the body/payload of the message.
     *
     * @return The body of the message.
     */
    String getBody();

    /**
     * Sets the body of the message.
     *
     * @param body The body to set.
     */
    void setBody(String body);

    /**
     * Boolean indicating this message is an <code>HttpMessage</code>.
     *
     * @return <code>true</code> if this message is an <code>HttpMessage</code>, <code>false</code> otherwise.
     */
    default boolean isHttp() {return false;}

    /**
     * Gives this <code>Message</code> as <code>HttpMessage</code>.
     *
     * @return This <code>Message</code> as <code>HttpMessage</code>, or <code>null</code> when this <code>Message</code>
     * is not an <code>HttpMessage</code>.
     *
     * @see #isHttp()
     */
    default HttpMessage getHttpMessage() {return null;}
}
