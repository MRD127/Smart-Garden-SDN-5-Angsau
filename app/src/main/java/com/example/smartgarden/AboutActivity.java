package com.example.smartgarden;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private TextView tvAppName;
    private TextView tvAppVersion;
    private TextView tvCopyright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAppName = findViewById(R.id.tvAppName);
        tvAppVersion = findViewById(R.id.tvAppVersion);
        tvCopyright = findViewById(R.id.tvCopyright);

        tvAppName.setText("Smart Garden");
        tvAppVersion.setText("Versi 1.0");
        tvCopyright.setText("© 2025 Smart Garden Inc.");

        // Inisialisasi tombol kembali
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
