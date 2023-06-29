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

import static android.Manifest.permission.CAMERA;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.sunlakesEatingTracker.MainActivity.DAY_ID_KEY;
import static com.example.sunlakesEatingTracker.MainActivity.EATING_ID_KEY;
import static com.example.sunlakesEatingTracker.config.AppConfig.ServerConfig.SERVER_URL;
import static java.lang.String.format;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.sunlakesEatingTracker.component.SendRequestTask;
import com.example.sunlakesEatingTracker.model.ApiError;
import com.example.sunlakesEatingTracker.model.Entry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.color.DynamicColors;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class QrActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int dayId;
    private int eatingId;

    private SurfaceView surfaceView;
    private TextView scannedValueTextView;
    private TextView dayIdEatingIdTextView;
    private Button addManualButton;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this);
        setContentView(R.layout.activity_qr);

        initReceivedExtras();
        initViews();
    }

    private void initReceivedExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dayId = extras.getInt(DAY_ID_KEY);
            eatingId = extras.getInt(EATING_ID_KEY);
        } else {
            dayId = 1;
            eatingId = 1;
        }
    }

    private void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        scannedValueTextView = findViewById(R.id.scannedValueTextView);
        dayIdEatingIdTextView = findViewById(R.id.dayIdEatingIdTextView);
        dayIdEatingIdTextView.setText(format(
                getString(R.string.day_id_eating_id_template), dayId, eatingId
        ));
        addManualButton = findViewById(R.id.addManualButton);
        addManualButton.setOnClickListener(ignored -> {
            EditText dialogInput = new EditText(QrActivity.this);
            dialogInput.setTextSize(32);
            dialogInput.setGravity(Gravity.CENTER);
            new AlertDialog.Builder(QrActivity.this)
                    .setTitle("Add manual")
                    .setMessage("Enter bracelet id")
                    .setView(dialogInput)
                    .setPositiveButton("Add", (ignored1, ignored2) ->
                            sendPostRequest(dialogInput.getText().toString()))
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QrActivity.this, CAMERA) == PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(
                                QrActivity.this,
                                new String[]{CAMERA},
                                REQUEST_CAMERA_PERMISSION
                        );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<>() {

            private String cachedLastData = "";

            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() == 0) {
                    scannedValueTextView.post(() ->
                            scannedValueTextView.setText(R.string.no_data));
                    return;
                }

                final String data = barcodes.valueAt(0).displayValue;
                scannedValueTextView.post(() ->
                        scannedValueTextView.setText(data));

                if (!cachedLastData.equals(data)) {
                    cachedLastData = data;
                    sendPostRequest(data);
                }
            }
        });
    }

    private void sendPostRequest(final String data) {
        int braceletId;
        try {
            braceletId = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return;
        }
        final Entry entry = new Entry(
                braceletId,
                dayId,
                eatingId
        );
        try {
            Optional<ApiError> optionalError = new SendRequestTask(
                    SERVER_URL, objectMapper
            ).execute(entry).get();
            if (optionalError.isPresent()) {
                ApiError apiError = optionalError.get();
                showErrorDialog(
                        apiError.getError(),
                        apiError.getMessage()
                );
            } else {
                showToast(entry.getBraceletId() + " OK");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog(
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );
        }
    }

    private void showErrorDialog(String title, String message) {
        runOnUiThread(() -> new AlertDialog
                .Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        (dialog, ignored) -> dialog.dismiss())
                .create().show()
        );
    }

    private void showToast(final String message) {
        runOnUiThread(() -> Toast.makeText(
                        getApplicationContext(),
                        message,
                        LENGTH_SHORT
                ).show()
        );
    }
}