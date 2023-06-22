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

package com.example.sunlakesEatingTracker;

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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class MainActivity extends AppCompatActivity {

    public final static String DAY_ID_KEY = "day_id";
    public final static String EATING_ID_KEY = "eating_id";

    private Button startButton;
    private RadioGroup dayRadioGroup;
    private RadioGroup eatingRadioGroup;
    private SwitchCompat autoCompleteSwitch;

    private int dayId;
    private int eatingId;

    private static final Map<Integer, Integer> dayRadioIdDayId = Map.of(
            day1Radio, 1,
            day2Radio, 2,
            day3Radio, 3,
            day4Radio, 4,
            day5Radio, 5,
            day6Radio, 6,
            day7Radio, 7
    );
    private static final Map<Integer, Integer> eatingRadioIdEatingId = Map.of(
            eating1Radio, 1,
            eating2Radio, 2,
            eating3Radio, 3
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(view -> {
            configureAllArguments();
            startActivity(
                    new Intent(MainActivity.this, QrActivity.class)
                            .putExtra(DAY_ID_KEY, dayId)
                            .putExtra(EATING_ID_KEY, eatingId)
            );
        });
        dayRadioGroup = findViewById(R.id.dayRadioGroup);
        eatingRadioGroup = findViewById(R.id.eatingRadioGroup);
        autoCompleteSwitch = findViewById(R.id.autoCompleteSwitch);
    }

    private void configureAllArguments() {
        if (autoCompleteSwitch.isChecked()) {
            dayId = 1; // FIXME stub
            eatingId = 1; // TODO implement auto-completion by today date
            return;
        }
        final int dayRadioId = dayRadioGroup.getCheckedRadioButtonId();
        dayId = dayRadioIdDayId.get(dayRadioId);
        final int eatingRadioId = eatingRadioGroup.getCheckedRadioButtonId();
        eatingId = eatingRadioIdEatingId.get(eatingRadioId);
    }

    public void checkIfEnabled(final View view) {
        final boolean state = !autoCompleteSwitch.isChecked();
        setClickableState(dayRadioGroup, state);
        setClickableState(eatingRadioGroup, state);
    }

    private void setClickableState(final RadioGroup radioGroup, final boolean state) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            final View childAt = radioGroup.getChildAt(i);
            childAt.setClickable(state);
            childAt.setEnabled(state);
        }
    }
}