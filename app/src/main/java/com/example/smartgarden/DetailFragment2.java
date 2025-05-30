package com.example.smartgarden;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        private FirebaseDatabase database;
        private TextView textCahaya, textTaman2, textKelembabanUdara, textSuhuUdara;

        private ProgressBar progressBar2; // Tambahkan ini
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inisialisasi View terlebih dahulu
            View view = inflater.inflate(R.layout.fragment2_detail, container, false);

            // Inisialisasi TextView Sensor
            textCahaya = view.findViewById(R.id.textCahaya);
            textTaman2 = view.findViewById(R.id.textTaman2);
            textKelembabanUdara = view.findViewById(R.id.textKelembabanUdara);
            textSuhuUdara = view.findViewById(R.id.textSuhuUdara);
            progressBar2 = view.findViewById(R.id.progressBar2);
            // Inisialisasi Firebase
            database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app");

            DatabaseReference sensorRef = database.getReference("Sensor");
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
    }

