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

package com.machnos.api.gateway.server.domain;

/**
 * Class that is used for all exceptions that occur in the Machnos Api Gateway.
 */
public class MachnosException extends RuntimeException {

    // 100_* reserved for common errors.
    public static final int WRAPPED_EXCEPTION = 100_000;

    // 200_* reserved for configuration errors.
    public static final int INVALID_INTERFACE = 200_000;

    // 301_* reserved for function errors.
    public static final int MISSING_VARIABLE = 301_000;
    public static final int MISSING_VALUE = 301_001;


    private final int errorCode;
    private final String[] variables;

    public MachnosException(int errorCode) {
        super();
        this.errorCode = errorCode;
        this.variables = null;
    }

    public MachnosException(int errorCode, String... variables) {
        super();
        this.errorCode = errorCode;
        this.variables = variables;
    }

    public MachnosException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.variables = null;
    }


    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return getClass().getName() + ": Reason " + this.errorCode;
    }
}
