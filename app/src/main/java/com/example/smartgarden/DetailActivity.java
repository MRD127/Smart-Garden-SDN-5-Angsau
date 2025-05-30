package com.example.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set menu item yang aktif sesuai halaman ini
        bottomNavigationView.setSelectedItemId(R.id.detail);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    startActivity(new Intent(DetailActivity.this, DasboardActivity.class));
                    return true;
                } else if (id == R.id.detail) {
                    // Sudah di halaman DetailActivity, jadi tidak perlu navigasi ulang
                    return true;
                } else if (id == R.id.history) {
                    startActivity(new Intent(DetailActivity.this, HistoryActivity.class));
                    return true;
                } else if (id == R.id.setting) {
                    startActivity(new Intent(DetailActivity.this, SettingActivity.class));
                    return true;
                }
                return false;
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DetailFragment())
                .commit();

    }
}
