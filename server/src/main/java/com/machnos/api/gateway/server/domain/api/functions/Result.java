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

package com.machnos.api.gateway.server.domain.api.functions;

public class Result {

    private static final int STATUS_CODE_FAILED = -1;
    private static final int STATUS_CODE_SUCCESS = 0;
    private static final int STATUS_CODE_STOP_API = 1;

    public static final Result FAILED = new Result(STATUS_CODE_FAILED);
    public static final Result SUCCESS = new Result(STATUS_CODE_SUCCESS);
    public static final Result STOP_API = new Result(STATUS_CODE_STOP_API);

    private final int statusCode;

    public Result(int statusCode) {
        this.statusCode = statusCode;
    }
}
