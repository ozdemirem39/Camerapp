package com.yalcay.camerapp;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyActivity extends AppCompatActivity {

    private String studyName;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private XSSFWorkbook workbook;
    private XSSFSheet rgbSheet, hsvSheet;
    private LinearLayout photoContainer;
    private List<Uri> photoUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        studyName = getIntent().getStringExtra("study_name");

        PreviewView previewView = findViewById(R.id.preview_view);
        photoContainer = findViewById(R.id.photo_container);

        workbook = new XSSFWorkbook();
        rgbSheet = workbook.createSheet("RGB");
        hsvSheet = workbook.createSheet("HSV");
        createExcelHeaders();

        cameraExecutor = Executors.newSingleThreadExecutor();
        startCamera(previewView);

        findViewById(R.id.capture_button).setOnClickListener(v -> takePhoto());
        findViewById(R.id.finish_work_button).setOnClickListener(v -> finishWork());
    }

    private void startCamera(PreviewView previewView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                androidx.camera.core.Preview preview = new androidx.camera.core.Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder().build();
                cameraProvider.bindToLifecycle(this, androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture);
            } catch (Exception e) {
                Toast.makeText(this, "Kamera başlatılamadı!", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Kamera hazır değil", Toast.LENGTH_SHORT).show();
            return;
        }

        String fileName = new SimpleDateFormat("HHmmss", Locale.US).format(new Date()) + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + studyName);

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
        ).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Uri savedUri = outputFileResults.getSavedUri();
                        if (savedUri != null) {
                            photoUris.add(savedUri);
                            addPhotoToContainer(savedUri);
                            processPhoto(savedUri);
                            Toast.makeText(getApplicationContext(), "Fotoğraf kaydedildi", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(getApplicationContext(), "Fotoğraf kaydedilemedi!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addPhotoToContainer(Uri photoUri) {
        ImageView photo = new ImageView(this);
        photo.setImageURI(photoUri);
        photo.setLayoutParams(new LinearLayout.LayoutParams(
                (int) getResources().getDisplayMetrics().density * 64,
                (int) getResources().getDisplayMetrics().density * 128
        ));
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photo.setPadding(8, 8, 8, 8);

        // Fotoğrafa tıklama ile silme özelliği
        photo.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Fotoğrafı Sil")
                .setMessage("Bu fotoğrafı silmek istediğinizden emin misiniz?")
                .setPositiveButton("Evet", (dialog, which) -> {
                    getContentResolver().delete(photoUri, null, null);
                    photoUris.remove(photoUri);
                    photoContainer.removeView(photo);
                    Toast.makeText(this, "Fotoğraf silindi.", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hayır", null)
                .show();
        });

        photoContainer.addView(photo);
    }

    private void processPhoto(Uri photoUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            // İşlem yapılacak RGB ve HSV verileri
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createExcelHeaders() {
        Row rgbHeaderRow = rgbSheet.createRow(0);
        String[] rgbHeaders = {"File Name", "R", "G", "B"};
        for (int i = 0; i < rgbHeaders.length; i++) {
            Cell cell = rgbHeaderRow.createCell(i);
            cell.setCellValue(rgbHeaders[i]);
        }
    }

    private void finishWork() {
        try {
            Uri excelUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, studyName + ".xlsx");
            values.put(MediaStore.Files.FileColumns.MIME_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Documents/" + studyName);

            Uri fileUri = getContentResolver().insert(excelUri, values);
            if (fileUri != null) {
                OutputStream fos = getContentResolver().openOutputStream(fileUri);
                workbook.write(fos);
                fos.close();
                Toast.makeText(this, "Excel dosyası kaydedildi.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Excel dosyası kaydedilemedi!", Toast.LENGTH_SHORT).show();
        } finally {
            finish();
        }
    }
}