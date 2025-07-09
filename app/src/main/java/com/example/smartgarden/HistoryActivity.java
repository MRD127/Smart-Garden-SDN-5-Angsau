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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout container;
    private final int MAX_LOGS_TO_KEEP = 10; // Batas maksimal riwayat yang disimpan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengaktifkan EdgeToEdge untuk tampilan full screen
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        // Mengatur agar status bar menjadi transparan
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Mengatur padding untuk system bars agar konten tidak tertimpa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        setupBottomNavigation();

        container = findViewById(R.id.historyContainer);

        loadHistoryLogs(); // Memulai listener realtime
    }

    private void setupBottomNavigation() {
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
    }

    private void loadHistoryLogs() {
        FirebaseDatabase database = FirebaseDatabase.getInstance(
                "https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app"
        );

        DatabaseReference refHistory = database.getReference("HistoryPenyiraman");

        refHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> logSnapshots = new ArrayList<>();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    logSnapshots.add(logSnapshot);
                }

                // --- FITUR HAPUS OTOMATIS ---
                if (logSnapshots.size() > MAX_LOGS_TO_KEEP) {
                    // Urutkan dari yang paling LAMA ke BARU untuk menemukan data yang akan dihapus
                    Collections.sort(logSnapshots, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));

                    int logsToDelete = logSnapshots.size() - MAX_LOGS_TO_KEEP;
                    Log.d("HistoryCleaner", "Jumlah log (" + logSnapshots.size() + ") melebihi batas. Menghapus " + logsToDelete + " log terlama.");

                    for (int i = 0; i < logsToDelete; i++) {
                        // Hapus data terlama dari Firebase
                        logSnapshots.get(i).getRef().removeValue();
                    }

                    // Hentikan eksekusi di sini. onDataChange akan dipanggil lagi setelah data terhapus,
                    // dan saat itu, kode akan lanjut ke bagian tampilan.
                    return;
                }
                // --- AKHIR FITUR HAPUS OTOMATIS ---

                // --- Logika untuk Menampilkan Data ---
                container.removeAllViews(); // Bersihkan tampilan

                // Urutkan dari yang paling BARU ke LAMA untuk ditampilkan
                Collections.sort(logSnapshots, (o1, o2) -> o2.getKey().compareTo(o1.getKey()));

                // Loop melalui data yang sudah diurutkan untuk ditampilkan
                for (DataSnapshot logSnapshot : logSnapshots) {
                    String tamanName = logSnapshot.child("taman").getValue(String.class);
                    String displayName = (tamanName != null) ? tamanName.replace("_", " ") : "Taman Tidak Dikenal";
                    addLogView(displayName, logSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDB", "Gagal membaca HistoryPenyiraman: " + error.getMessage());
            }
        });
    }

    private void addLogView(String tamanName, DataSnapshot logSnapshot) {
        Long durasi = logSnapshot.child("durasi_detik").getValue(Long.class);
        Long kelembapanAwal = logSnapshot.child("kelembapan_tanah_awal").getValue(Long.class);
        Long kelembapanAkhir = logSnapshot.child("kelembapan_tanah_akhir").getValue(Long.class);
        Double suhuUdara = logSnapshot.child("suhu_udara").getValue(Double.class);
        Double kelembapanUdara = logSnapshot.child("kelembapan_udara").getValue(Double.class);

        Object mulaiMillisObj = logSnapshot.child("waktu_mulai_ms").getValue();

        long mulaiMillis = 0;
        if (mulaiMillisObj instanceof Number) {
            mulaiMillis = ((Number) mulaiMillisObj).longValue();
        }

        if (mulaiMillis < 1000000000000L) {
            Log.w("HistoryActivity", "Melewatkan log dengan waktu tidak valid, key: " + logSnapshot.getKey());
            return;
        }

        long selesaiMillis = mulaiMillis + ((durasi != null ? durasi : 0) * 1000);

        String mulaiFormatted = epochToDateTime(mulaiMillis);
        String selesaiFormatted = epochToTime(selesaiMillis);

        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setPadding(16, 16, 16, 16);
        tv.setTextColor(Color.DKGRAY);
        tv.setBackgroundColor(Color.WHITE);

        String text = "Taman: " + tamanName + "\n" +
                "Waktu: " + mulaiFormatted + " - " + selesaiFormatted + "\n" +
                "Durasi: " + (durasi != null ? durasi : "-") + " detik\n" +
                "Kelembapan Tanah: " + (kelembapanAwal != null ? kelembapanAwal : "-") + "% → " + (kelembapanAkhir != null ? kelembapanAkhir : "-") + "%\n" +
                "Suhu Udara: " + (suhuUdara != null ? String.format(Locale.getDefault(), "%.1f", suhuUdara) : "-") + " °C\n" +
                "Kelembapan Udara: " + (kelembapanUdara != null ? String.format(Locale.getDefault(), "%.1f", kelembapanUdara) : "-") + " %";
        tv.setText(text);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
        params.setMargins(0, 0, 0, 16);
        tv.setLayoutParams(params);

        container.addView(tv);
    }

    private String epochToDateTime(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }

    private String epochToTime(long epochMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(epochMillis));
    }
}
