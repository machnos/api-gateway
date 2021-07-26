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

/**
 * Class representing the result of a <code>Function</code> execution.
 */
public class Result {

    /**
     * The status code for failed results.
     */
    private static final int STATUS_CODE_FAILED = -1;
    /**
     * The status coded for succeeded results.
     */
    private static final int STATUS_CODE_SUCCESS = 0;
    /**
     * The status code for results that should stop execution of the <code>Api</code> immediately.
     */
    private static final int STATUS_CODE_STOP = 1;

    /**
     * The <code>Result</code> instance for successful <code>Function</code> executions.
     */
    private static final Result SUCCESS = new Result(STATUS_CODE_SUCCESS);

    /**
     * The status code.
     */
    private final int statusCode;

    /**
     * The reason for the result.
     */
    private String reason;

    /**
     * Constructs a new <code>Result</code> instance.
     * @param statusCode The status code for the <code>Result</code>.
     */
    public Result(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Get the reason of the <code>Result</code>.
     * @return The reason of the result.
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Set the reason of a result.
     * @param reason The reason.
     * @return This <code>Result</code> instance.
     */
    private Result setReason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Boolean indicating this <code>Result</code> represents a failed <code>Api</code> execution.
     *
     * @return <code>true</code> when the <code>Result</code> represents an <code>Api</code> execution failure, <code>false</code> otherwise.
     */
    public boolean isFailed() {
        return this.statusCode == STATUS_CODE_FAILED;
    }

    /**
     * Boolean indicating this <code>Result</code> represents a succeeded <code>Api</code> execution.
     *
     * @return <code>true</code> when the <code>Result</code> represents a successful <code>Api</code> execution,
     * <code>false</code> otherwise.
     */
    public boolean isSucceeded() {
        return this.statusCode == STATUS_CODE_SUCCESS;
    }

    /**
     * Boolean indicating this <code>Result</code> represents an <code>Api</code> execution that should stop any
     * further execution of other <code>Function</code>s.
     *
     * @return <code>true</code> when the <code>Result</code> represents an <code>Api</code> execution that should stop,
     * <code>false</code> otherwise.
     */
    public boolean isStopApi() {
        return this.statusCode == STATUS_CODE_STOP;
    }

    /**
     * Gives a <code>Result</code> with the {@link #STATUS_CODE_SUCCESS} status code.
     *
     * @return A <code>Result</code> with the {@link #STATUS_CODE_SUCCESS} status code.
     */
    public static Result succeed() {
        return SUCCESS;
    }

    /**
     * Gives a <code>Result</code> with the {@link #STATUS_CODE_FAILED} status code.
     *
     * @param reason The reason of the failure.
     * @return A <code>Result</code> with the {@link #STATUS_CODE_FAILED} status code.
     */
    public static Result fail(String reason) {
        return new Result(STATUS_CODE_FAILED).setReason(reason);
    }

    /**
     * Gives a <code>Result</code> with the {@link #STATUS_CODE_STOP} status code.
     *
     * @param reason The reason to stop the <code>Api</code>.
     * @return A <code>Result</code> with the {@link #STATUS_CODE_STOP} status code.
     */
    public static Result stop(String reason) {
        return new Result((STATUS_CODE_STOP)).setReason(reason);
    }
}
