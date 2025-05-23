package com.example.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class DasboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Gunakan if-else untuk menghindari error "Constant expression required"
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(DasboardActivity.this, DasboardActivity.class));
                    return true;
                } else if (id == R.id.detail) {
                    startActivity(new Intent(DasboardActivity.this, DetailActivity.class));
                    return true;
                } else if (id == R.id.history) {
                    startActivity(new Intent(DasboardActivity.this, HistoryActivity.class));
                    return true;
                } else if (id == R.id.setting) {
                    startActivity(new Intent(DasboardActivity.this, SettingActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
