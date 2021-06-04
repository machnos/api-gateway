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

public class SetResponseContent extends AbstractFunction {

    private String content;
    private String contentType;

    public SetResponseContent() {
        super("SetResponseContent");
    }

    @Override
    public Result execute(ExecutionContext executionContext) {
        executionContext.getResponseMessage().setBody(executionContext.parse(getContent()));
        if (executionContext.getResponseMessage().isHttp()) {
            final var httpResponse = executionContext.getResponseMessage().getHttpMessage();
            if (getContentType() != null) {
                httpResponse.getHeaders().set(Headers.HTTP_CONTENT_TYPE, executionContext.parse(getContentType()) + "; charset=" + Charset.defaultCharset());
            }
        }
        return Result.SUCCESS;
    }

    public String getContent() {
        return this.content;
    }

    public SetResponseContent setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContentType() {
        return this.contentType;
    }

    public SetResponseContent setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
