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

package com.example.sunlakesEatingTracker.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

public class FestivalConfig {

    public static final Map<LocalDate, Integer> dayDateDayId = Map.of(
            LocalDate.of(2023, 06, 22), 1,
            LocalDate.of(2023, 06, 23), 2,
            LocalDate.of(2023, 06, 24), 3,
            LocalDate.of(2023, 06, 25), 4,
            LocalDate.of(2023, 06, 26), 5,
            LocalDate.of(2023, 06, 27), 6,
            LocalDate.of(2023, 06, 28), 7
    );

    /**
     * Eating ranges:
     * <p>
     * breakfast:  00:00-11:00
     * lunch:      11:01-14:00
     * dinner:     14:01-23:59
     */
    public static final TreeMap<LocalTime, Integer> eatingTimeEatingId = new TreeMap<>(Map.of(
            LocalTime.of(00, 00), 1,
            LocalTime.of(11, 00), 1,
            LocalTime.of(11, 01), 2,
            LocalTime.of(14, 00), 2,
            LocalTime.of(14, 01), 3,
            LocalTime.of(23, 59), 3
    ));
}
