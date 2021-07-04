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

import com.machnos.api.gateway.server.domain.transport.x509.PublicKey;

/**
 * <code>VariableHandler</code> implementation that can handle values of a <code>PublicKey</code> instance.
 */
public class PublicKeyVariableHandler extends AbstractVariableHandler<PublicKey> {

    /**
     * The algorithm variable.
     */
    public static final String ALGORITHM = "algorithm";
    /**
     * The size variable.
     */
    public static final String SIZE = "size";

    @Override
    @SuppressWarnings("unchecked")
    public <I> I getValue(String variable, PublicKey publicKey) {
        if (variable == null || publicKey == null) {
            return null;
        } else if (NO_VARIABLE.equals(variable)) {
            return (I) publicKey;
        } else if (ALGORITHM.equals(variable)) {
            return (I) publicKey.getAlgorithm();
        } else if (SIZE.equals(variable)) {
            return (I) Integer.valueOf(publicKey.getKeySize());
        }
        return null;
    }
}