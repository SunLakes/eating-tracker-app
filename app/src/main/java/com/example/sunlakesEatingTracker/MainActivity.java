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

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.sunlakesEatingTracker.config.FestivalConfig.dayDateDayId;
import static com.example.sunlakesEatingTracker.config.FestivalConfig.eatingTimeEatingId;
import static com.example.sunlakesEatingTracker.config.MainActivityConfig.dayRadioIdDayId;
import static com.example.sunlakesEatingTracker.config.MainActivityConfig.eatingRadioIdEatingId;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map.Entry;
import java.util.Optional;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(ignored -> {
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

        predefineArgumentsViaDate();
    }

    private void configureAllArguments() throws DateTimeException {
        final int dayRadioId = dayRadioGroup.getCheckedRadioButtonId();
        dayId = dayRadioIdDayId.get(dayRadioId);
        final int eatingRadioId = eatingRadioGroup.getCheckedRadioButtonId();
        eatingId = eatingRadioIdEatingId.get(eatingRadioId);
    }

    private void predefineArgumentsViaDate() {
        int predefinedDayId = Optional.ofNullable(dayDateDayId.get(LocalDate.now()))
                .orElse(1);
        int predefinedEatingId = Optional.ofNullable(
                eatingTimeEatingId.ceilingEntry(LocalTime.now()).getValue()
        ).orElse(1);
        int dayRadioId = dayRadioIdDayId.entrySet()
                .stream()
                .filter(entry -> predefinedDayId == entry.getValue())
                .map(Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "RadioButton that corresponds to day_id=%d doesn't exists"));
        int eatingRadioId = eatingRadioIdEatingId.entrySet()
                .stream()
                .filter(entry -> predefinedEatingId == entry.getValue())
                .map(Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "RadioButton that corresponds to eating_id=%d doesn't exists"));
        RadioButton dayRadioButton = findViewById(dayRadioId);
        RadioButton eatingRadioButton = findViewById(eatingRadioId);
        dayRadioButton.setChecked(true);
        eatingRadioButton.setChecked(true);
    }

    public void disableRadioIfChecked(final View ignored) {
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

    private void showToast(final String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(),
                        message,
                        LENGTH_SHORT
                ).show()
        );
    }
}