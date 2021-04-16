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

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/**
 * Abstract superclass for all <code>KeyStoreWrapper</code> instances. This class holds the actual <code>KeyStore</code>
 * instance and delegate persistent actions to the underlying class.
 */
public abstract class AbstractKeyStoreWrapper implements KeyStoreWrapper {

    /**
     * The actual keystore that is wrapped.
     */
    private final KeyStore keyStore;

    /**
     * Constructs a new <code>KeyStoreWrapper</code> instance. The instance is <b>not</b> initialized after this
     * construct call. Implementing classes need to handle this based on their storage implementation.
     *
     * @param type The <code>KeyStoreTypo</code> used to instantiate the actual <code>KeyStore</code>.
     */
    AbstractKeyStoreWrapper(KeyStoreType type) {
        try {
            this.keyStore = KeyStore.getInstance(type.name());
        } catch (KeyStoreException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    @Override
    public KeyStore getKeyStore() {
        return this.keyStore;
    }

    @Override
    public void setKeyEntry(String alias, Key key, char[] password, Certificate[] chain) {
        try {
            this.keyStore.setKeyEntry(alias, key, password, chain);
            persist();
        } catch (KeyStoreException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    @Override
    public void deleteEntry(String alias) {
        try {
            this.keyStore.deleteEntry(alias);
            persist();
        } catch (KeyStoreException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    /**
     * Persist the keystore to the underlying storage.
     */
    protected abstract void persist();
}
