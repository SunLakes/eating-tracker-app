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

package com.example.sunlakesEatingTracker.component;

import android.os.AsyncTask;

import com.example.sunlakesEatingTracker.model.ApiError;
import com.example.sunlakesEatingTracker.model.Entry;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class SendRequestTask extends AsyncTask<Entry, Integer, Optional<ApiError>> {

    private final ObjectMapper objectMapper;

    private final String url;

    public SendRequestTask(final String url,
                           final ObjectMapper objectMapper) {
        this.url = url;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Optional<ApiError> doInBackground(Entry... entry) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setDoOutput(true);
            connection.addRequestProperty(
                    "Content-type", "application/json");
            connection.getRequestProperty("POST");

            OutputStreamWriter outputStream = new OutputStreamWriter(
                    connection.getOutputStream());

            outputStream.write(objectMapper.writeValueAsString(entry[0]));
            outputStream.close();

            InputStream inputStream;
            try {
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                inputStream = connection.getErrorStream();
            }

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            final String answer = bufferedReader.lines().collect(Collectors.joining());
            inputStream.close();

            if (connection.getResponseCode() == 200) { // OK
                return Optional.empty();
            } else {
                return Optional.of(
                        objectMapper.readValue(answer, ApiError.class)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.of(
                    new ApiError(e.getClass().getSimpleName(), e)
            );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}