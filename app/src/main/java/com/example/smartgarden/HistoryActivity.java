package com.example.smartgarden;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengaktifkan Edge-to-Edge mode
        EdgeToEdge.enable(this);

        // Membuat status bar transparan
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Menentukan layout
        setContentView(R.layout.activity_history);

        // Hanya padding kiri, atas, kanan — TANPA bottom
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        // Menambahkan BottomNavigationView dan listener navigasi
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set menu item yang aktif sesuai halaman ini
        bottomNavigationView.setSelectedItemId(R.id.history);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    startActivity(new Intent(HistoryActivity.this, DasboardActivity.class));
                    return true;
                } else if (id == R.id.history) {
                    return true;
                } else if (id == R.id.detail) {
                    startActivity(new Intent(HistoryActivity.this, DetailActivity.class));
                    return true;
                } else if (id == R.id.setting) {
                    startActivity(new Intent(HistoryActivity.this, SettingActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
