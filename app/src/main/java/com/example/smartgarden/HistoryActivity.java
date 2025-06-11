package com.example.smartgarden;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.history);

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        });

        container = findViewById(R.id.historyContainer);

        loadHistoryLogs(); // Realtime listener
    }

    private void loadHistoryLogs() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

        DatabaseReference refHistory = database.getReference("HistoryPenyiraman");

        refHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                container.removeAllViews(); // Clear before re-adding

                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    String tamanName = logSnapshot.child("taman").getValue(String.class);
                    if (tamanName != null) {
                        String displayName = tamanName.replace("_", " ");
                        addLogView(displayName, logSnapshot);
                    } else {
                        addLogView("Unknown Taman", logSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDB", "Failed to read HistoryPenyiraman: " + error.getMessage());
            }
        });
    }

    private void addLogView(String tamanName, DataSnapshot logSnapshot) {
        Long durasi = logSnapshot.child("durasi_detik").getValue(Long.class);
        Integer kelembapanAwal = logSnapshot.child("kelembapan_tanah_awal").getValue(Integer.class);
        Integer kelembapanAkhir = logSnapshot.child("kelembapan_tanah_akhir").getValue(Integer.class);
        Double suhuUdara = logSnapshot.child("suhu_udara").getValue(Double.class);
        Double kelembapanUdara = logSnapshot.child("kelembapan_udara").getValue(Double.class);

        // Gunakan waktu dari sistem (bukan dari Firebase)
        long mulaiMillis = System.currentTimeMillis();
        long selesaiMillis = mulaiMillis + (durasi != null ? durasi * 1000 : 0);

        String mulaiFormatted = epochToDate(mulaiMillis);
        String selesaiFormatted = epochToDate(selesaiMillis);

        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setPadding(16, 16, 16, 16);
        tv.setTextColor(Color.DKGRAY);
        tv.setBackgroundColor(Color.WHITE);
        tv.setText(
                "Taman: " + tamanName + "\n" +
                        "Mulai: " + mulaiFormatted + "\n" +
                        "Selesai: " + selesaiFormatted + "\n" +
                        "Durasi: " + (durasi != null ? durasi + " detik" : "-") + "\n" +
                        "Kelembapan Tanah: " + (kelembapanAwal != null ? kelembapanAwal : "-") + "% → " +
                        (kelembapanAkhir != null ? kelembapanAkhir : "-") + "%\n" +
                        "Suhu Udara: " + (suhuUdara != null ? suhuUdara + " °C" : "-") + "\n" +
                        "Kelembapan Udara: " + (kelembapanUdara != null ? kelembapanUdara + " %" : "-")
        );

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.setMargins(0, 0, 0, 16);
        tv.setLayoutParams(params);

        container.addView(tv);
    }

    private Long getLongValue(DataSnapshot snapshot) {
        if (snapshot == null || snapshot.getValue() == null) return null;
        Object value = snapshot.getValue();
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private String epochToDate(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date(epochMillis);
        return sdf.format(date);
    }
}
