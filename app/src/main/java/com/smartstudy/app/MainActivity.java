package com.smartstudy.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.smartstudy.app.reader.PdfReaderActivity;

import com.smartstudy.app.core.AppController;
import com.smartstudy.app.core.SessionController;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStartReading);

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    PdfReaderActivity.class
            );
            startActivity(intent);
        });


        // 🔹 TEST MEMBER 1 CORE LOGIC (THIS IS THE KEY)
        SessionController.getInstance().startSession();
        // AppController.getInstance().onLowLightDetected();
        Log.d(TAG, "Core test executed");

        // 🔹 UI (unchanged)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 🔹 TEMPORARY: Launch PDF Reader to test Member 2
        startActivity(new Intent(this, PdfReaderActivity.class));

        //Member3
        // com.smartstudy.app.notes.NotesNavigator.openNotesForPage(this, 0);

    }
}
