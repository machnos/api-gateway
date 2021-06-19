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

import com.machnos.api.gateway.server.domain.message.Headers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock <code>Headers</code> implementation with setter methods for all properties.
 */
public class MockHeaders implements Headers {

    /**
     * An in memory map of headers.
     */
    private Map<String, List<String>> headers = new HashMap<>();

    @Override
    public boolean contains(String headerName) {
        return this.headers.containsKey(headerName);
    }

    @Override
    public List<String> get(String headerName) {
        return this.headers.get(headerName);
    }

    @Override
    public String get(String headerName, int index) {
        if (this.headers.containsKey(headerName)) {
            final var values = this.headers.get(headerName);
            if (values == null || index < 0 || index >= values.size() ) {
                return null;
            }
            return values.get(index);
        }
        return null;
    }

    @Override
    public void set(String headerName, String headerValue) {
        final var values = new ArrayList<String>();
        values.add(headerValue);
        this.headers.put(headerName, values);
    }

    @Override
    public void set(String headerName, List<String> headerValues) {
        this.headers.put(headerName, headerValues);
    }

    @Override
    public List<String> getHeaderNames() {
        return new ArrayList<>(this.headers.keySet());
    }

    @Override
    public int getSize() {
        return this.headers.size();
    }
}
