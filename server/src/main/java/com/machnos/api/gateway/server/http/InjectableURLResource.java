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

import io.undertow.UndertowLogger;
import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.resource.URLResource;
import io.undertow.util.StatusCodes;
import org.xnio.IoUtils;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * <code>URLResource</code> implementation that is capable of replacing variables with actual values.
 */
public class InjectableURLResource extends URLResource {

    /**
     * The name of the cluster.
     */
    private final String clusterName;

    /**
     * The URL to wrap.
     */
    private final URL url;

    /**
     * Constructs a new <code>InjectableURLResource</code> instance.
     *
     * @param url The <code>URL</code> to wrap.
     * @param path The path.
     * @param clusterName The name of the Machnos Api Gateway cluster.
     */
    public InjectableURLResource(final URL url, final String path, final String clusterName) {
        super(url, path);
        this.url = url;
        this.clusterName = clusterName;
    }

    @Override
    public Long getContentLength() {
        // The content length is unknown for files that have properties being injected.
        return couldBeInjected() ? null : super.getContentLength();
    }

    @Override
    public File getFile() {
        // The file is unknown for files that have properties being injected.
        return couldBeInjected() ? null : super.getFile();
    }

    @Override
    public Path getFilePath() {
        // The file path is unknown for files that have properties being injected.
        return couldBeInjected() ? null : super.getFilePath();
    }


    @Override
    public void serve(Sender sender, HttpServerExchange exchange, IoCallback completionCallback) {
        if (couldBeInjected()) {
            serveImpl(sender, exchange, -1, -1, false, completionCallback);
        } else {
            super.serve(sender, exchange, completionCallback);
        }
    }

    @Override
    public void serveRange(Sender sender, HttpServerExchange exchange, long start, long end, IoCallback completionCallback) {
        if (couldBeInjected()) {
            serveImpl(sender, exchange, start, end, true, completionCallback);
        } else {
            super.serveRange(sender, exchange, start, end, completionCallback);
        }
    }

    /**
     * Boolean indicating the resource should be checked for injectable variables.
     *
     * @return <code>true</code> when the resource should be inspected, <code>false</code> otherwise.
     */
    private boolean couldBeInjected() {
        return getPath().toLowerCase().endsWith(".html");
    }

    @Override
    public void serveImpl(final Sender sender, final HttpServerExchange exchange, final long start, final long end, final boolean range, final IoCallback completionCallback) {

        class ServerTask implements Runnable, IoCallback {

            private InputStream inputStream;
            private byte[] buffer;

            long toSkip = start;
            final long remaining = end - start + 1;

            @Override
            public void run() {
                if (range && remaining == 0) {
                    //we are done, just return
                    IoUtils.safeClose(inputStream);
                    completionCallback.onComplete(exchange, sender);
                    return;
                }
                if (inputStream == null) {
                    try (BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                        String htmlContent = buffer.lines().collect(Collectors.joining("\n"));
                        htmlContent = htmlContent.replaceAll("@clusterName", InjectableURLResource.this.clusterName);
                        inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
                        return;
                    }
                    buffer = new byte[1024];//TODO: we should be pooling these
                }
                try {
                    int res = inputStream.read(buffer);
                    if (res == -1) {
                        //we are done, just return
                        IoUtils.safeClose(inputStream);
                        completionCallback.onComplete(exchange, sender);
                        return;
                    }
                    int bufferStart = 0;
                    int length = res;
                    if (range && toSkip > 0) {
                        //skip to the start of the requested range
                        //not super efficient, but what can you do
                        while (toSkip > res) {
                            toSkip -= res;
                            res = inputStream.read(buffer);
                            if (res == -1) {
                                //we are done, just return
                                IoUtils.safeClose(inputStream);
                                completionCallback.onComplete(exchange, sender);
                                return;
                            }
                        }
                        bufferStart = (int) toSkip;
                        length -= toSkip;
                        toSkip = 0;
                    }
                    if (range && length > remaining) {
                        length = (int) remaining;
                    }
                    sender.send(ByteBuffer.wrap(buffer, bufferStart, length), this);
                } catch (IOException e) {
                    onException(exchange, sender, e);
                }

            }

            @Override
            public void onComplete(final HttpServerExchange exchange, final Sender sender) {
                if (exchange.isInIoThread()) {
                    exchange.dispatch(this);
                } else {
                    run();
                }
            }

            @Override
            public void onException(final HttpServerExchange exchange, final Sender sender, final IOException exception) {
                UndertowLogger.REQUEST_IO_LOGGER.ioException(exception);
                IoUtils.safeClose(inputStream);
                if (!exchange.isResponseStarted()) {
                    exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
                }
                completionCallback.onException(exchange, sender, exception);
            }
        }

        ServerTask serveTask = new ServerTask();
        if (exchange.isInIoThread()) {
            exchange.dispatch(serveTask);
        } else {
            serveTask.run();
        }
    }
}
