package com.example.smartgarden;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


    public class DetailFragment2 extends Fragment {
        public DetailFragment2() {
        }
        private TextView textStatusTaman2;
        private FirebaseDatabase database;
        private TextView textCahaya, textTaman2, textKelembabanUdara, textSuhuUdara;
        private TextView textPumpAir;
        private ProgressBar progressBar2; // Tambahkan ini
        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inisialisasi View terlebih dahulu
            View view = inflater.inflate(R.layout.fragment2_detail, container, false);

            // Inisialisasi TextView Sensor
            textCahaya = view.findViewById(R.id.textCahaya);
            textPumpAir = view.findViewById(R.id.textPumpAir2);
            textTaman2 = view.findViewById(R.id.textTaman2);
            textKelembabanUdara = view.findViewById(R.id.textKelembabanUdara);
            textSuhuUdara = view.findViewById(R.id.textSuhuUdara);
            progressBar2 = view.findViewById(R.id.progressBar2);

            textStatusTaman2 = view.findViewById(R.id.textStatusTaman2);

            // Inisialisasi Firebase
            database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference taman2RelayRef = database.getReference("Control").child("Relay").child("Taman_1");
            DatabaseReference sensorRef = database.getReference("Sensor");

            //Baca data realtime dari Firebase
            taman2RelayRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Boolean status = snapshot.getValue(Boolean.class);
                    if (status != null) {
                        textPumpAir.setText(status ? "ON" : "OFF");
                    } else {
                        textPumpAir.setText("No Data");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Gagal membaca data Taman_1", error.toException());
                }
            });
            sensorRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Cahaya
                    Integer cahaya = snapshot.child("Cahaya").getValue(Integer.class);
                    textCahaya.setText(cahaya != null ? cahaya + " lx" : "Data tidak tersedia");

                    // Taman 2 (kelembapan tanah)
                    Integer taman2 = snapshot.child("Kelembapan_Tanah").child("Taman_2").getValue(Integer.class);
                    if (taman2 != null) {
                        textTaman2.setText(taman2 + "%");
                        progressBar2.setProgress(taman2); // Update progress bar
                        updateStatusText(textStatusTaman2, taman2);
                    } else {
                        textTaman2.setText("Data tidak tersedia");
                        progressBar2.setProgress(0); // Reset jika data tidak ada
                    }

                    // Kelembaban Udara
                    Double kelembabanUdara = snapshot.child("Kelembapan_Udara").getValue(Double.class);
                    textKelembabanUdara.setText(kelembabanUdara != null ? kelembabanUdara + "%" : "Data tidak tersedia");

                    // Suhu Udara
                    Double suhuUdara = snapshot.child("Suhu_Udara").getValue(Double.class);
                    textSuhuUdara.setText(suhuUdara != null ? suhuUdara + "°C" : "Data tidak tersedia");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    textCahaya.setText("Gagal");
                    textTaman2.setText("Gagal");
                    textKelembabanUdara.setText("Gagal");
                    textSuhuUdara.setText("Gagal");
                    progressBar2.setProgress(0);
                }
            });

            return view; // Return view yang sudah di-setup
        }

        private void updateStatusText(TextView statusView, int value) {
            // Set font Poppins SemiBold


            if (value < 30) {
                statusView.setText("Rendah");
                statusView.setTextColor(getResources().getColor(android.R.color.white));
            } else if (value < 50) {
                statusView.setText("Minim");
                statusView.setTextColor(getResources().getColor(android.R.color.white));
            } else if (value <= 70) {
                statusView.setText("Ideal");
                statusView.setTextColor(getResources().getColor(android.R.color.white));
            } else if (value <= 85) {
                statusView.setText("Tinggi");
                statusView.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                statusView.setText("Tinggi");
                statusView.setTextColor(getResources().getColor(android.R.color.white));
            }
        }
    }

