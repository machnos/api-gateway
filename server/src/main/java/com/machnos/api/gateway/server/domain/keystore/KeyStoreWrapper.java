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

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

/**
 * Interface for all classes that wrap a standard Java Key Store.
 *
 * The main reason for the existence of this class is the lack of synchronization options in the standard Java Key
 * Stores with, for example, the filesystem.
 */
public interface KeyStoreWrapper {

    /**
     * The supported Key Store types.
     */
    enum KeyStoreType {JKS, PKCS12}

    /**
     * Gives the wrapped <code>KeyStore</code>.
     * @return The Java <code>KeyStore</code> instances that is wrapped by this class.
     */
    KeyStore getKeyStore();

    /**
     * Sets a new <code>PrivateKey</code> and a corresponding certificate chain on this <code>KeyStoreWrapper</code>.
     * When the alias is already present, the current key, password and chain will be replaced with the given new
     * values.
     * @param alias The alias under which the key and certificate should be stored.
     * @param key THe key to store.
     * @param password The password to protect the key.
     * @param chain The certificate chain for the corresponding public key (only required if the given key is of type
     *              java.security.PrivateKey)
     *
     * @see KeyStore#setKeyEntry(String, Key, char[], Certificate[])
     */
    void setKeyEntry(String alias, Key key, char[] password, Certificate[] chain);

    /**
     * Delete an entry from the keystore.
     *
     * @param alias The alias of the entry to delete.
     *
     * @see KeyStore#deleteEntry(String) 
     */
    void deleteEntry(String alias);
}
