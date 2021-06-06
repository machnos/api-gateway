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

package com.machnos.api.gateway.server;

import com.machnos.api.gateway.server.configuration.Configuration;
import com.machnos.api.gateway.server.http.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Main entry point of the Machnos Api Gateway. This class provided the main method that will launch the server.
 */
public class Launcher {

    /**
     * The logger for this class.
     */
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        try {
            configuration = Configuration.load(new File(Configuration.CONFIG_DIRECTORY, "machnos.yml"));
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Unable to load configuration", e);
            }
        }
        final var server = new Server(configuration);
        server.start();
    }


}

