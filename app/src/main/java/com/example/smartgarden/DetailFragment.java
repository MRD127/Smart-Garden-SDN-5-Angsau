package com.example.smartgarden;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
    }
    private TextView textStatusTaman1;
    private FirebaseDatabase database;
    private TextView textCahaya, textTaman1, textKelembabanUdara, textSuhuUdara;
    private TextView textPumpAir;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout dan simpan ke dalam variabel
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Inisialisasi TextView Sensor
        textCahaya = view.findViewById(R.id.textCahaya);
        textPumpAir = view. findViewById(R.id.textPumpAir);
        textTaman1 = view.findViewById(R.id.textTaman1);
        textKelembabanUdara = view.findViewById(R.id.textKelembabanUdara);
        textSuhuUdara = view.findViewById(R.id.textSuhuUdara);
        progressBar = view.findViewById(R.id.progressBar);

        textStatusTaman1 = view.findViewById(R.id.textStatusTaman1);
        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference sensorRef = database.getReference("Sensor");

        DatabaseReference taman1RelayRef = database.getReference("Control").child("Relay").child("Taman_1");

// Baca data realtime dari Firebase
        taman1RelayRef.addValueEventListener(new ValueEventListener() {
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
                if (cahaya != null) {
                    textCahaya.setText(cahaya + " lx");
                } else {
                    textCahaya.setText("Data tidak tersedia");
                }

                // Taman 2 (kelembapan tanah)
                Integer taman1 = snapshot.child("Kelembapan_Tanah").child("Taman_1").getValue(Integer.class);
                if (taman1 != null) {
                    textTaman1.setText(taman1 + "%");
                    progressBar.setProgress(taman1); // Update progress bar
                    updateStatusText(textStatusTaman1, taman1);
                } else {
                    textTaman1.setText("Data tidak tersedia");
                    progressBar.setProgress(0); // Reset jika data tidak ada
                }

                // Kelembaban Udara
                Double kelembabanUdara = snapshot.child("Kelembapan_Udara").getValue(Double.class);
                if (kelembabanUdara != null) {
                    textKelembabanUdara.setText(kelembabanUdara + "%");
                } else {
                    textKelembabanUdara.setText("Data tidak tersedia");
                }

                // Suhu Udara
                Double suhuUdara = snapshot.child("Suhu_Udara").getValue(Double.class);
                if (suhuUdara != null) {
                    textSuhuUdara.setText(suhuUdara + "°C");
                } else {
                    textSuhuUdara.setText("Data tidak tersedia");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textCahaya.setText("Gagal");
                textTaman1.setText("Gagal");
                textKelembabanUdara.setText("Gagal");
                textSuhuUdara.setText("Gagal");
            }
        });

        // Kembalikan view yang sudah diinisialisasi
        return view;
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





