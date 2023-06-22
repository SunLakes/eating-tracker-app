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

import static com.example.sunlakesEatingTracker.R.id.day1Radio;
import static com.example.sunlakesEatingTracker.R.id.day2Radio;
import static com.example.sunlakesEatingTracker.R.id.day3Radio;
import static com.example.sunlakesEatingTracker.R.id.day4Radio;
import static com.example.sunlakesEatingTracker.R.id.day5Radio;
import static com.example.sunlakesEatingTracker.R.id.day6Radio;
import static com.example.sunlakesEatingTracker.R.id.day7Radio;
import static com.example.sunlakesEatingTracker.R.id.eating1Radio;
import static com.example.sunlakesEatingTracker.R.id.eating2Radio;
import static com.example.sunlakesEatingTracker.R.id.eating3Radio;

import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class MainActivityConfig {

    public static final Map<Integer, Integer> dayRadioIdDayId = Map.of(
            day1Radio, 1,
            day2Radio, 2,
            day3Radio, 3,
            day4Radio, 4,
            day5Radio, 5,
            day6Radio, 6,
            day7Radio, 7
    );

    public static final Map<Integer, Integer> eatingRadioIdEatingId = Map.of(
            eating1Radio, 1,
            eating2Radio, 2,
            eating3Radio, 3
    );
}
