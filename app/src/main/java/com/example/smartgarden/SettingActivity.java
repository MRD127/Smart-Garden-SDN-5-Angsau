package com.example.smartgarden;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_setting);

        // Menghapus padding bawah agar BottomNavigationView nempel ke bawah
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        // Navigasi ke halaman Personal Info
        LinearLayout personalInfoLayout = findViewById(R.id.layout_personal_info);
        personalInfoLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });

        // Navigasi ke halaman Help
        LinearLayout helpLayout = findViewById(R.id.layout_help);
        helpLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
            startActivity(intent);
        });

        // Navigasi ke halaman About
        LinearLayout aboutLayout = findViewById(R.id.layout_about);
        aboutLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        // Logout
        Button logoutButton = findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Sign out Firebase user
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear stack
            startActivity(intent);
            finish();
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.setting);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(SettingActivity.this, DasboardActivity.class));
                    return true;
                } else if (id == R.id.detail) {
                    startActivity(new Intent(SettingActivity.this, DetailActivity.class));
                    return true;
                } else if (id == R.id.history) {
                    startActivity(new Intent(SettingActivity.this, HistoryActivity.class));
                    return true;
                } else if (id == R.id.setting) {
                    return true;
                }
                return false;
            }
        });
    }
}
