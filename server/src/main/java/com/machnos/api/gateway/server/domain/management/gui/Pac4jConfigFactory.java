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

package com.machnos.api.gateway.server.domain.management.gui;

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

public class Pac4jConfigFactory implements ConfigFactory {

    private final String baseUrl;

    public Pac4jConfigFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Config build(Object... parameters) {
        final var formClient = new FormClient(this.baseUrl + "/login/login.html", new SimpleTestUsernamePasswordAuthenticator());
        final var clients = new Clients(this.baseUrl + "/callback", formClient);

        final var config = new Config(clients);
        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer("ADMIN"));
        return config;
    }
}
