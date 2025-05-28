package com.example.smartgarden;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PersonalInfoActivity extends AppCompatActivity {

    private EditText etName, etEmail;
    private ImageView profileImageView;
    private Button btnChangeProfilePicture, btnSaveChanges;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private static final int IMAGE_REQUEST_CODE = 1000;
    private static final String PROFILE_IMAGE_NAME = "profile_picture.png";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        profileImageView = findViewById(R.id.profileImage);
        btnChangeProfilePicture = findViewById(R.id.btnChangeProfilePicture);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Buat email tidak bisa diedit
        etEmail.setFocusable(false);
        etEmail.setClickable(false);
        etEmail.setCursorVisible(false);
        etEmail.setKeyListener(null);

        btnChangeProfilePicture.setOnClickListener(v -> openImagePicker());
        btnSaveChanges.setOnClickListener(v -> saveUserProfile());

        if (currentUser != null) {
            loadUserProfile(currentUser.getUid());
        } else {
            Toast.makeText(this, "Pengguna tidak login", Toast.LENGTH_SHORT).show();
        }

        loadProfileImage();
        setupBottomNavigation();
    }

    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        String email = document.getString("email");

                        etName.setText(name != null ? name : "");
                        etEmail.setText(email != null ? email : "");
                    } else {
                        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
                );
    }

    private void saveUserProfile() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim(); // Tetap ambil email untuk validasi jika perlu

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nama dan Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUser != null) {
            String userId = currentUser.getUid();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            // Email tidak diperbarui di sini
            db.collection("users").document(userId)
                    .update(userMap)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    );
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            if (data != null && data.getData() != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    profileImageView.setImageBitmap(bitmap);
                    saveProfileImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveProfileImage(Bitmap bitmap) {
        File file = new File(getFilesDir(), PROFILE_IMAGE_NAME);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(this, "Foto profil berhasil disimpan", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan foto profil", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileImage() {
        File file = new File(getFilesDir(), PROFILE_IMAGE_NAME);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImageView.setImageBitmap(bitmap);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.setting); // Tandai item aktif

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    startActivity(new Intent(PersonalInfoActivity.this, DasboardActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.detail) {
                    startActivity(new Intent(PersonalInfoActivity.this, DetailActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.history) {
                    startActivity(new Intent(PersonalInfoActivity.this, HistoryActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.setting) {
                    // Sudah di halaman ini
                    return true;
                }
                return false;
            }
        });
    }
}
