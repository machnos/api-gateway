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

package com.machnos.api.gateway.server.domain.api.functions;

import com.machnos.api.gateway.server.domain.api.ExecutionContext;
import com.machnos.api.gateway.server.domain.message.Headers;

import java.nio.charset.Charset;

/**
 * <code>Function</code> that sets the response content.
 */
public class SetResponseContent extends AbstractFunction {

    /**
     * The name of this <code>Function</code>.
     */
    private static final String FUNCTION_NAME = "Set response content";

    /**
     * The content to send to the client.
     */
    private String content;

    /**
     * The content type of the content.
     */
    private String contentType;

    public SetResponseContent() {
        super(FUNCTION_NAME);
    }

    @Override
    public Result doExecute(ExecutionContext executionContext) {
        executionContext.getResponseMessage().setBody(executionContext.parse(getContent()));
        if (executionContext.getResponseMessage().isHttp()) {
            final var httpResponse = executionContext.getResponseMessage().getHttpMessage();
            if (getContentType() != null) {
                httpResponse.getHeaders().set(Headers.HTTP_CONTENT_TYPE, executionContext.parse(getContentType()) + "; charset=" + Charset.defaultCharset());
            }
        }
        return Result.succeed();
    }

    /**
     * Gives the content.
     *
     * @return The content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the content.
     *
     * @param content The content to send to the client.
     * @return This <code>SetResponseContent</code> instance.
     */
    public SetResponseContent setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Gives the content type.
     * @return The content type.
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Sets the content type.
     *
     * @param contentType The content type.
     * @return This <code>SetResponseContent</code> instance.
     */
    public SetResponseContent setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
