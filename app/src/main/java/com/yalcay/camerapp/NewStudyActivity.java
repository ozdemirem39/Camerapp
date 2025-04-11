package com.yalcay.camerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewStudyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_study);

        EditText studyNameInput = findViewById(R.id.study_name_input);
        Button startStudyButton = findViewById(R.id.start_study_button);

        startStudyButton.setOnClickListener(v -> {
            String studyName = studyNameInput.getText().toString().trim();

            if (studyName.isEmpty()) {
                studyName = new SimpleDateFormat("yyyyMMddHHmm", Locale.US).format(new Date());
            }

            Intent intent = new Intent(NewStudyActivity.this, StudyActivity.class);
            intent.putExtra("study_name", studyName);
            startActivity(intent);
        });
    }
}