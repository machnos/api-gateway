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

package com.machnos.api.gateway.server.domain.keystore;

import com.machnos.api.gateway.server.domain.MachnosException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * <code>KeyStoreWrapper</code> implementation that stores all keys in memory.
 */
public class InMemoryKeyStoreWrapper extends AbstractKeyStoreWrapper {

    /**
     * Constructs a new <code>InMemoryKeyStore</code> instance.
     * @param password The password that should be used for the keystore, or <code>null</code> when no password is required.
     */
    public InMemoryKeyStoreWrapper(char[] password) {
        super(KeyStoreType.JKS);
        try {
            getKeyStore().load(null, password);
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    @Override
    protected void persist() {
        // NO-OP, everything is stored in memory.
    }
}
