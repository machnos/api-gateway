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

package com.machnos.api.gateway.server.domain.api;

import com.machnos.api.gateway.server.domain.api.variables.VariableParser;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.Transport;

public class ExecutionContext {

    private final Transport transport;
    private final Message requestMessage;
    private final Message responseMessage;
    private final VariableParser variableParser = new VariableParser();
    private Account account;
    private Api api;

    public ExecutionContext(Transport transport, Message requestMessage, Message responseMessage) {
        this.transport = transport;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
    }

    public Transport getTransport() {
        return this.transport;
    }

    public Message getRequestMessage() {
        return this.requestMessage;
    }

    public Message getResponseMessage() {
        return this.responseMessage;
    }

    public ExecutionContext setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Account getAccount() {
        return this.account;
    }

    public Api getApi() {
        return this.api;
    }

    public void executeApi(Api api) {
        this.api = api;
        api.handleRequest(this);
    }


    public String parse(String input) {
        return this.variableParser.parse(input, this);
    }
}
