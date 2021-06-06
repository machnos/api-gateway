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

package com.machnos.api.gateway.server.domain.idm;

/**
 * An <code>Account</code> represents an user of system identity.
 */
public class Account {

    /**
     * The username of the account.
     */
    private final String username;
    /**
     * The credentials of the account.
     */
    private final Credentials credentials;

    /**
     * Constructs a new <code>Account</code> instance.
     *
     * @param username The username of the user/system the <code>Account/<code> belongs to.
     * @param credentials The credentials for authenticating the user/system.
     */
    public Account(String username, Credentials credentials) {
        this.username = username;
        this.credentials = credentials;
    }

    /**
     * Gives the username of the <code>Account</code>.
     *
     * @return The username of the <code>Account</code>.
     */
    public String getUsername() {
        return this.username;
    }
}
