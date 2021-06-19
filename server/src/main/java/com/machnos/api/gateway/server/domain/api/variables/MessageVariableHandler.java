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

package com.machnos.api.gateway.server.domain.api.variables;

import com.machnos.api.gateway.server.domain.message.Message;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>Message</code> instance.
 */
public class MessageVariableHandler extends AbstractVariableHandler<Message> {

    /**
     * The prefix for the header variables.
     */
    public static final String PREFIX_HEADER = "header" + AbstractVariableHandler.OBJECT_DIVIDER;
    /**
     * The length of the prefix for the header variables.
     */
    public static final int PREFIX_HEADER_LENGTH = PREFIX_HEADER.length();
    /**
     * The body variable.
     */
    public static final String BODY = "body";
    /**
     * The is http variable.
     */
    public static final String IS_HTTP = "ishttp";

    @Override
    public Object getValue(String variable, Message message) {
        if (variable == null || message == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return message;
        } else if (BODY.equals(variable)) {
            return message.getBody();
        } else if (variable.startsWith(PREFIX_HEADER)) {
            // Headers form a collection. First check if we need to provide the size.
            if (variable.endsWith(AbstractVariableHandler.SUFFIX_COLLECTION_SIZE)) {
                final var headerName = variable.substring(PREFIX_HEADER_LENGTH, variable.length() - SUFFIX_COLLECTION_SIZE_LENGTH);
                final var values = message.getHeaders().get(headerName);
                if (values == null) {
                    return 0;
                }
                return values.size();
            }
            //TODO support {.header[n]} format

            // Return the header list.
            return message.getHeaders().get(variable.substring(PREFIX_HEADER_LENGTH));
        } else if (IS_HTTP.equals(variable)) {
            return message.isHttp();
        }
        return null;
    }
}
