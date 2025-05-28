package com.example.smartgarden;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    private TextView tvUserName;
    private ImageView profileImageView;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setSelectedItemId(R.id.setting);

        tvUserName = findViewById(R.id.profileName);
        profileImageView = findViewById(R.id.profileImage);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            tvUserName.setText(name != null ? name : "Nama tidak tersedia");
                        } else {
                            tvUserName.setText("Data tidak ditemukan");
                        }
                    })
                    .addOnFailureListener(e -> tvUserName.setText("Gagal memuat nama"));
        }

        // Bottom navigation handler
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.detail) {
                startActivity(new Intent(this, DetailActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.history) {
                startActivity(new Intent(this, HistoryActivity.class));
                finish();
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.setting) {
                return true;
            }
            return false;
        });

        // Logout
        Button logoutButton = findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DasboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Navigasi tiap item pengaturan
        findViewById(R.id.personalInfo).setOnClickListener(v ->
                startActivity(new Intent(this, PersonalInfoActivity.class)));

        findViewById(R.id.notifications).setOnClickListener(v ->
                startActivity(new Intent(this, NotificationsActivity.class)));

        findViewById(R.id.help).setOnClickListener(v ->
                startActivity(new Intent(this, HelpActivity.class)));

        findViewById(R.id.about).setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));

        // Set ikon dan teks item
        setupSettingItem(R.id.personalInfo, R.drawable.ic_profile, "Personal Information", "Your profile information");
        setupSettingItem(R.id.notifications, R.drawable.ic_notifications, "Notifications", "Notification settings");
        setupSettingItem(R.id.help, R.drawable.ic_help, "Help", "Data preferences and storage settings");
        setupSettingItem(R.id.about, R.drawable.ic_info, "About", "Version 1.0");

        // Tampilkan foto profil
        loadProfileImage();
    }

    private void setupSettingItem(int itemId, int iconRes, String title, String desc) {
        LinearLayout itemView = findViewById(itemId);
        TextView titleView = itemView.findViewById(R.id.settingTitle);
        TextView descView = itemView.findViewById(R.id.settingDesc);
        View iconView = itemView.findViewById(R.id.settingIcon);

        titleView.setText(title);
        descView.setText(desc);
        iconView.setBackgroundResource(iconRes);
    }

    private void loadProfileImage() {
        File file = new File(getFilesDir(), "profile_picture.png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImage(); // Perbarui gambar saat kembali ke halaman
    }
}
