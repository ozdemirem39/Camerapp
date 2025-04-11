package com.yalcay.camerapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<PhotoData> photoDataList = new ArrayList<>();
    private File outputFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ana menüdeki düğmeleri tanımla
        Button newStudyButton = findViewById(R.id.new_study_button);
        Button calibrateButton = findViewById(R.id.calibrate_button);
        Button rgbToConcentrationButton = findViewById(R.id.rgb_to_concentration_button);
        Button capturePhotoButton = findViewById(R.id.capture_photo_button); // Yeni fotoğraf çekme düğmesi
        Button exportToExcelButton = findViewById(R.id.export_to_excel_button); // Yeni Excel'e yazma düğmesi

        // New Study düğmesine tıklandığında
        newStudyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewStudyActivity.class);
            startActivity(intent);
        });

        // Calibrate ve RGB to Concentration işlemleri henüz tanımlanmadığı için şimdilik boş bırakıldı
        calibrateButton.setOnClickListener(v -> {
            // Calibrate Activity açılacak (Henüz tamamlanmadı)
        });

        rgbToConcentrationButton.setOnClickListener(v -> {
            // RGB to Concentration Activity açılacak (Henüz tamamlanmadı)
        });

        // Fotoğraf çek düğmesine tıklandığında
        capturePhotoButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        // Excel'e yaz düğmesine tıklandığında
        exportToExcelButton.setOnClickListener(v -> {
            if (!photoDataList.isEmpty() && outputFile != null) {
                try {
                    PhotoProcessor.processAndWriteToXLSX(photoDataList, outputFile);
                    Toast.makeText(this, "Data exported to: " + outputFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to export data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No data to export!", Toast.LENGTH_SHORT).show();
            }
        });

        // Output dosya yolu
        outputFile = new File(getFilesDir(), "output.xlsx");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Örnek RGB değerleri (gerçek uygulamada yeşil dikdörtgen üzerinden alınmalı)
            int r1 = 255, g1 = 0, b1 = 0;  // Örnek: Kırmızı
            int r2 = 0, g2 = 255, b2 = 0;  // Örnek: Yeşil
            int r3 = 0, g3 = 0, b3 = 255;  // Örnek: Mavi

            // Fotoğraf verisini listeye ekle
            photoDataList.add(new PhotoData("photo_" + System.currentTimeMillis() + ".jpg", r1, g1, b1, r2, g2, b2, r3, g3, b3));

            Toast.makeText(this, "Photo processed and added to the list!", Toast.LENGTH_SHORT).show();
        }
    }
}