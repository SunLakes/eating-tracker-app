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

import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class QrActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int dayId;
    private int eatingId;

    private SurfaceView surfaceView;
    private TextView scannedValueTextView;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        initReceivedExtras();
        initViews();
    }

    private void initReceivedExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dayId = extras.getInt("day_id");
            eatingId = extras.getInt("eating_id");
        } else {
            showToast("An default args initialized");
            dayId = 1;
            eatingId = 1;
        }
    }

    private void initViews() {
        scannedValueTextView = findViewById(R.id.scannedValueTextView);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {
        showToast("Barcode scanner started");

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
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            private String cachedLastData = "";

            @Override
            public void release() {
                showToast("To prevent memory leaks barcode scanner has been stopped");
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
                    System.out.println("recognized new");
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
            showToast("Not a number");
            return;
        }
        final Entry entry = new Entry(
                braceletId,
                dayId,
                eatingId
        );
        try {
            // FIXME hardcode url
            Optional<ApiError> optionalError = new SendRequestTask(
                    "http://192.168.1.50:8080/eating", objectMapper
            ).execute(entry).get();
            if (optionalError.isPresent()) {
                showErrorDialog(optionalError.get().toString());
            } else {
                showToast(entry.getBraceletId() + " OK");
            }
        } catch (ExecutionException | InterruptedException e) {
            showErrorDialog(e.getMessage());
        }
    }

    private void showErrorDialog(final String message) {
        runOnUiThread(() ->
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(message)
                        .setNegativeButton("OK",
                                (dialog, which) -> dialog.dismiss())
                        .create().show()
        );
    }

    private void showToast(final String message) {
        runOnUiThread(() ->
                Toast.makeText(getApplicationContext(),
                        message,
                        LENGTH_SHORT
                ).show()
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}