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
import com.machnos.api.gateway.server.domain.api.variables.Variables;
import com.machnos.api.gateway.server.domain.idm.Account;
import com.machnos.api.gateway.server.domain.message.Message;
import com.machnos.api.gateway.server.domain.transport.Transport;

/**
 * The context in which an <code>Api</code> is executed.
 */
public class ExecutionContext {

    /**
     * The <code>Transport</code> from the client to the <code>Api</code>.
     */
    private final Transport transport;
    /**
     * The request <code>Message</code> sent to the <code>Api</code>.
     */
    private final Message requestMessage;
    /**
     * The response <code>Message</code> to be send to the client.
     */
    private final Message responseMessage;
    /**
     * The variable parser that can transform variables into values.
     */
    private final VariableParser variableParser = new VariableParser();
    /**
     * The custom variables an <code>Api</code> has set.
     */
    private final Variables variables = new Variables();
    /**
     * The account that is used to authenticate the remote user calling the <code>Api</code>.
     */
    private Account account;
    /**
     * The <code>Api</code> that is executed.
     */
    private Api api;

    /**
     * Constructs a new <code>ExecutionContext</code> instance.
     *
     * @param transport The <code>Transport</code> from the client to the <code>Api</code>.
     * @param requestMessage The request <code>Message</code> sent to the <code>Api</code>.
     * @param responseMessage The response <code>Message</code> to be sent to the client.
     */
    public ExecutionContext(Transport transport, Message requestMessage, Message responseMessage) {
        this.transport = transport;
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
    }

    /**
     * Gives the <code>Transport</code> that was used to connect to the <code>Api</code>.
     *
     * @return The <code>Transport</code> that was used to connect to the <code>Api</code>.
     */
    public Transport getTransport() {
        return this.transport;
    }

    /**
     * Gives the request <code>Message</code> that was sent to the <code>Api</code>.
     *
     * @return The request <code>Message</code>.
     */
    public Message getRequestMessage() {
        return this.requestMessage;
    }

    /**
     * Gives the response <code>Message</code> that will be send to to the client.
     *
     * @return The response <code>Message</code>.
     */
    public Message getResponseMessage() {
        return this.responseMessage;
    }

    /**
     * Gives the <code>Variables</code> that are set by the <code>Api</code>.
     *
     * @return THe <code>Variables</code>.
     */
    public Variables getVariables() {
        return this.variables;
    }

    /**
     * Sets the <code>Account</code> that represents the identity of the remote user who is calling the <code>Api</code>.
     *
     * @param account The <code>Account</code>.
     * @return This <code>ExecutionContext</code> instance.
     */
    public ExecutionContext setAccount(Account account) {
        this.account = account;
        return this;
    }

    /**
     * Gives the <code>Account</code> that represents the identity of the remote user who is calling the <code>Api</code>.
     * @return The <code>Account</code>.
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * Gives the <code>Api</code> that is currently executed.
     *
     * @return The <code>Api</code>.
     */
    public Api getApi() {
        return this.api;
    }

    /**
     * Execute an <code>Api</code> in this <code>ExecutionContext</code>.
     *
     * @param api The <code>Api</code> to execute.
     */
    public void executeApi(Api api) {
        this.api = api;
        api.handleRequest(this);
        this.api = null;
    }

    /**
     * Parse an input string that might contain variables. The variables will be replaces and the whole will be returned
     * as string.
     *
     * @param input The input to parse.
     * @return The parsed input.
     */
    public String parseVariableAsString(String input) {
        return this.variableParser.parseAsString(input, this);
    }
}
