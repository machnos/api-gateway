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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * <code>KeyStoreWrapper</code> implementation that stores all keys on the filesystem. Changes on the filesystem are
 * reflected on the wrapped <code>KeyStore</code> instance in such a way that the {@link #getKeyStore()} method always
 * returns the latest entries from the filesystem.
 *
 * Be aware that the keystore password will be kept in memory to support the file synchronization. Memory dumps of the
 * Java runtime will contain this password!
 */
public class FileSystemKeyStoreWrapper extends AbstractKeyStoreWrapper {

    /**
     * The <code>File</code> that is used to load and store the <code>KeyStore</code> entries to/from.
     */
    private final File keyStoreFile;

    /**
     * The password used for the <code>KeyStore</code> integrity.
     */
    private final char[] password;

    /**
     * Boolean indicating the current entries are all loaded from file.
     */
    private boolean loadedFromFile;

    /**
     * The last time the {@link #keyStoreFile} was changed on the filesystem.
     */
    private long lastModified;

    /**
     * Constructs a new <code>FileSystemKeyStoreWrapper</code> instance.
     *
     * @param keyStoreFile The <code>File</code> to load and store the <code>KeyStore</code> entries from/to.
     * @param keyStoreType The <code>KeyStore</code> type.
     * @param password The password to check the keystore integrity.
     */
    public FileSystemKeyStoreWrapper(File keyStoreFile, KeyStoreType keyStoreType, char[] password) {
        super(keyStoreType);
        this.keyStoreFile = keyStoreFile;
        this.password = password;
        loadKeyStore();
    }

    /**
     * Loads the key store. If the key store file not exists the keystore is initialized empty.
     */
    private void loadKeyStore() {
        if (this.keyStoreFile.exists()) {
            try (var fileInputStream = new FileInputStream(this.keyStoreFile)) {
                super.getKeyStore().load(fileInputStream, this.password);
                this.lastModified = this.keyStoreFile.lastModified();
                this.loadedFromFile = true;
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
        } else {
            try {
                super.getKeyStore().load(null, this.password);
                this.lastModified = -1;
                this.loadedFromFile = false;
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
        }
    }

    @Override
    protected void persist() {
        if (!this.keyStoreFile.getParentFile().exists()) {
            this.keyStoreFile.getParentFile().mkdirs();
            try {
                this.keyStoreFile.createNewFile();
            } catch (IOException e) {
                throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
            }
        }
        try (var fileOutputStream = new FileOutputStream(this.keyStoreFile)) {
            super.getKeyStore().store(fileOutputStream, this.password);
            this.lastModified = this.keyStoreFile.lastModified();
            this.loadedFromFile = true;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new MachnosException(MachnosException.WRAPPED_EXCEPTION, e);
        }
    }

    /**
     * Gives the wrapped <code>KeyStore</code>. This method also checks if the keystore is changed on the filesystem.
     * If so, the keystore will be reloaded before returned to the calling class.
     *
     * @return The wrapped <code>KeyStore</code>.
     */
    @Override
    public KeyStore getKeyStore() {
        if (this.keyStoreFile.exists()) {
            // There is a keystore file, check if we need to reload it. This would be the case if the file is changed
            // since the last time we loaded, or when it was created on the filesystem after we've loaded it.
            if (this.lastModified != this.keyStoreFile.lastModified() || !this.loadedFromFile) {
                loadKeyStore();
            }
        } else if (this.loadedFromFile) {
            // There's no keystore file, so we only should reload it if it previously was loaded from a file.
            loadKeyStore();
        }
        return super.getKeyStore();
    }
}
