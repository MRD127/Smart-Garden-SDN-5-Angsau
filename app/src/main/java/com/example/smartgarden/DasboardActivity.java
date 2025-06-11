package com.example.smartgarden;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.Manifest;

public class DasboardActivity extends AppCompatActivity {
    private TextView textCityName, textTemp, textDesc, textTemp1;
    private TextView textStatusTaman1, textStatusTaman2;
    private ImageView imageWeather, imageProfile;
    private FirebaseDatabase database;
    private TextView textCahaya, textTaman1, textTaman2, textKelembabanUdara, textSuhuUdara;

    private final String API_KEY = "47730936a35a0e34ea8ecbf7e9945d19";
    private final String CITY_NAME = "Pelaihari";

    private boolean sudahNotifikasiTaman1 = false;
    private boolean sudahNotifikasiTaman2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        // Inisialisasi view
        textCityName = findViewById(R.id.textCityName);
        textTemp = findViewById(R.id.textTemp);
        textTemp1 = findViewById(R.id.textTemp1);
        textDesc = findViewById(R.id.textDesc);
        imageWeather = findViewById(R.id.imageWeather);
        imageProfile = findViewById(R.id.imageProfile); // Inisialisasi foto profil

        textStatusTaman1 = findViewById(R.id.textStatusTaman1);
        textStatusTaman2 = findViewById(R.id.textStatusTaman2);

        textCahaya = findViewById(R.id.textCahaya);
        textTaman1 = findViewById(R.id.textTaman1);
        textTaman2 = findViewById(R.id.textTaman2);
        textKelembabanUdara = findViewById(R.id.textKelembabanUdara);
        textSuhuUdara = findViewById(R.id.textSuhuUdara);

        // Load gambar profil dari internal storage
        File file = new File(getFilesDir(), "profile_picture.png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageProfile.setImageBitmap(bitmap);
        }

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app");

        // Izin notif
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        // Notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "smart_garden_channel",
                    "Smart Garden Notifikasi",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        DatabaseReference sensorRef = database.getReference("Sensor");
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer cahaya = snapshot.child("Cahaya").getValue(Integer.class);
                textCahaya.setText(cahaya != null ? cahaya + " lx" : "Data tidak tersedia");

                Integer taman1 = snapshot.child("Kelembapan_Tanah").child("Taman_1").getValue(Integer.class);
                textTaman1.setText(taman1 != null ? taman1 + " %" : "Data tidak tersedia");
                if (taman1 != null) updateStatusText(textStatusTaman1, taman1);

                Integer taman2 = snapshot.child("Kelembapan_Tanah").child("Taman_2").getValue(Integer.class);
                textTaman2.setText(taman2 != null ? taman2 + " %" : "Data tidak tersedia");
                if (taman2 != null) updateStatusText(textStatusTaman2, taman2);

                Double kelembabanUdara = snapshot.child("Kelembapan_Udara").getValue(Double.class);
                textKelembabanUdara.setText(kelembabanUdara != null ? kelembabanUdara + "%" : "Data tidak tersedia");

                Double suhuUdara = snapshot.child("Suhu_Udara").getValue(Double.class);
                textSuhuUdara.setText(suhuUdara != null ? suhuUdara + "°C" : "Data tidak tersedia");

                DatabaseReference relayRef = database.getReference("Control/Relay");
                relayRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Boolean taman1 = snapshot.child("Taman_1").getValue(Boolean.class);
                        Boolean taman2 = snapshot.child("Taman_2").getValue(Boolean.class);

                        if (Boolean.TRUE.equals(taman1) && !sudahNotifikasiTaman1) {
                            sendLocalNotification("Penyiraman Taman 1", "Sistem sedang menyiram Taman 1.");
                            sudahNotifikasiTaman1 = true;
                        } else if (Boolean.FALSE.equals(taman1)) {
                            sudahNotifikasiTaman1 = false;
                        }

                        if (Boolean.TRUE.equals(taman2) && !sudahNotifikasiTaman2) {
                            sendLocalNotification("Penyiraman Taman 2", "Sistem sedang menyiram Taman 2.");
                            sudahNotifikasiTaman2 = true;
                        } else if (Boolean.FALSE.equals(taman2)) {
                            sudahNotifikasiTaman2 = false;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Tambahkan log jika perlu
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textCahaya.setText("Gagal");
                textTaman1.setText("Gagal");
                textTaman2.setText("Gagal");
                textKelembabanUdara.setText("Gagal");
                textSuhuUdara.setText("Gagal");
            }
        });

        loadWeather();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(getFilesDir(), "profile_picture.png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageProfile.setImageBitmap(bitmap);
        }
    }

    private void updateStatusText(TextView statusView, int value) {
        statusView.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_semibold));

        if (value < 30) {
            statusView.setText("Rendah");
        } else if (value < 50) {
            statusView.setText("Minim");
        } else if (value <= 70) {
            statusView.setText("Ideal");
        } else {
            statusView.setText("Tinggi");
        }

        statusView.setTextColor(getResources().getColor(android.R.color.white));
    }

    private void loadWeather() {
        WeatherService service = RetrofitInstance.getWeatherService();
        Call<WeatherResponse> call = service.getWeather(CITY_NAME, API_KEY, "metric");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    double temp = weather.getMain().getTemp();
                    String description = weather.getWeather().get(0).getDescription();
                    String iconCode = weather.getWeather().get(0).getIcon();

                    textCityName.setText(weather.getName());
                    textTemp.setText(String.format("%.0f°C", temp));
                    textDesc.setText(capitalize(description));
                    textTemp1.setText(String.format("%.0f°C", temp));

                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                    Picasso.get().load(iconUrl).into(imageWeather);
                } else {
                    textTemp.setText("?");
                    textDesc.setText("Gagal memuat cuaca");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textTemp.setText("?");
                textDesc.setText("Error: " + t.getMessage());
            }
        });
    }

    private void sendLocalNotification(String title, String message) {
        // Cek apakah izin notifikasi telah diberikan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Izin belum diberikan, jangan kirim notifikasi
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "smart_garden_channel")
                .setSmallIcon(R.drawable.splash_logo) // Ganti dengan ikon yang tersedia di drawable
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(100, builder.build());
    }



    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
