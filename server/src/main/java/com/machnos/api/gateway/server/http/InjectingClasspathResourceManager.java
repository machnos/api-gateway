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

package com.machnos.api.gateway.server.http;

import io.undertow.UndertowMessages;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.server.handlers.resource.ResourceChangeListener;
import io.undertow.server.handlers.resource.ResourceManager;

import java.io.IOException;
import java.net.URL;

public class InjectingClasspathResourceManager implements ResourceManager {

    private final String rootPackage;
    private final String clusterName;

    public InjectingClasspathResourceManager(String rootPackage, String clusterName) {
        if (rootPackage.isEmpty()) {
            this.rootPackage = "";
        } else if (rootPackage.endsWith("/")) {
            this.rootPackage = rootPackage;
        } else {
            this.rootPackage = rootPackage + "/";
        }
        this.clusterName = clusterName;
    }

    @Override
    public Resource getResource(String path) throws IOException {
        String modPath = path;
        if(modPath.startsWith("/")) {
            modPath = path.substring(1);
        }
        final String realPath = this.rootPackage + modPath;
        final URL resource = getClass().getClassLoader().getResource(realPath);
        if(resource == null) {
            return null;
        } else {
            return new InjectableURLResource(resource, path, this.clusterName);
        }
    }

    @Override
    public boolean isResourceChangeListenerSupported() {
        return false;
    }

    @Override
    public void registerResourceChangeListener(ResourceChangeListener listener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    @Override
    public void removeResourceChangeListener(ResourceChangeListener listener) {
        throw UndertowMessages.MESSAGES.resourceChangeListenerNotSupported();
    }

    @Override
    public void close() throws IOException {
    }
}
