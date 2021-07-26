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

package com.machnos.api.gateway.server.configuration;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Data object that holds all configuration that is (de)serialized as yaml file. This class will reflect the
 * configuration of the application, and can be used by the system administrator to alter the behaviour of the Api Gateway.
 */
public final class Configuration {

    /**
     * The key under which the config directory is located as property.
     */
    public static final File CONFIG_DIRECTORY = new File(System.getProperty("machnos.configDirectory", "./config/"));

    /**
     * The name of the cluster this node belongs to.
     */
    public String clusterName = System.getProperty("machnos.clusterName");

    /**
     * The name of the node. This name should be unique within a cluster.
     */
    public String nodeName = System.getProperty("machnos.nodeName", InetAddress.getLoopbackAddress().getHostName());

    /**
     * The configuration for the management interface. The management interface will be used to configure the Api Gateway
     * in a browser, or via an Json API.
     */
    public HttpInterface management = new HttpInterface();

    /**
     * The http interfaces that handle api requests.
     */
    public List<HttpInterface> httpInterfaces = new ArrayList<>();

    /**
     * Loads the <code>Configuration</code> object from file.
     *
     * @param configurationFile The file holding the configuration.
     * @return The <code>Configuration</code> instance.
     * @throws IOException When the configurationFile does not exists, cannot be read, or cannot be parsed.
     */
    public static Configuration load(File configurationFile) throws IOException {
        if (!configurationFile.exists() || !configurationFile.isFile() || !configurationFile.canRead()) {
            throw new FileNotFoundException(configurationFile.getAbsolutePath());
        }
        Configuration configuration;
        final var constructor = new Constructor();
        final var yaml = new Yaml(constructor);
        try (Reader reader = new FileReader(configurationFile)) {
            configuration = yaml.loadAs(reader, Configuration.class);
        }
        return configuration;
    }
}
