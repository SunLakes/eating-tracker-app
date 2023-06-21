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

package ua.mibal.peopleService.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class ApiError {

    private String status;

    private String timestamp;

    private String message;

    public ApiError(String status, Throwable e) {
        Date date = Calendar.getInstance().getTime();
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
        this.status = status;
        this.message = e.getMessage();
    }

    private ApiError() {
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ApiError{");
        sb.append("status=").append(status);
        sb.append(", timestamp='").append(timestamp).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
