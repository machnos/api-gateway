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

import com.machnos.api.gateway.server.domain.api.functions.CompoundFunction;
import com.machnos.api.gateway.server.domain.api.functions.SetResponseContent;
import com.machnos.api.gateway.server.domain.api.functions.flowlogic.AllFunctionsMustSucceed;
import com.machnos.api.gateway.server.domain.api.functions.security.RequireBasicAuthentication;
import com.machnos.api.gateway.server.domain.api.functions.security.RequireTransportSecurity;

public class DummyWebApi implements Api {

    private final CompoundFunction rootFunction = new AllFunctionsMustSucceed();

    public DummyWebApi() {
        this.rootFunction.addFunction(new RequireBasicAuthentication());
        this.rootFunction.addFunction(new RequireTransportSecurity().setRemoteCertificateRequired(true));
        this.rootFunction.addFunction(new SetResponseContent().setContent(
                "<p>Hi ${account.username}!</p>"
                + "<p>You visited this page using a '${transport.http.request.method}' method via interface '${transport.interfaceAlias}' and landed on api ${api.name}@${api.contextRoot}</p>"
                + "<p>The authorization header: ${request.header.authorization.first}</p>"
                + "<p>RequestUrl: ${transport.http.request.url}</p>"
                + "<p>RequestScheme: ${transport.http.request.url.scheme}</p>"
                + "<p>RequestHost: ${transport.http.request.url.host}</p>"
                + "<p>RequestPort: ${transport.http.request.url.port}</p>"
                + "<p>RequestPath: ${transport.http.request.url.path}</p>"
                + "<p>RequestQuery: ${transport.http.request.url.query}</p>"
                + "<p>RequestQueryParam3: ${transport.http.request.url.query.param3}</p>"
                + "<p>IsSecure: ${transport.isSecure}</p>"
                + "<p>CipherSuite: ${transport.security.cipherSuite}</p>"
                + "<p>SecurityProtocol: ${transport.security.protocol}</p>"
                + "<p>Alg: ${transport.security.localCertificate.key.algorithm}</p>"
                + "<p>Size: ${transport.security.localCertificate.key.size}</p>"
        ).setContentType("text/html"));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getContextRoot() {
        return "/api/";
    }

    @Override
    public void handleRequest(ExecutionContext executionContext) {
        this.rootFunction.execute(executionContext);
    }
}
