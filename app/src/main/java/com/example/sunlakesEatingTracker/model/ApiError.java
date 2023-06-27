/*
 * Copyright (c) 2023. http://t.me/mibal_ua
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.sunlakesEatingTracker.model;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ApiError {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError(Exception e) {
        this.timestamp = Timestamp.from(Instant.now()).toString();
        this.status = -1;
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.path = "";
    }

    private ApiError() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ApiError{" +
               "timestamp='" + timestamp + '\'' +
               ", status=" + status +
               ", error='" + error + '\'' +
               ", message='" + message + '\'' +
               ", path='" + path + '\'' +
               '}';
    }
}
