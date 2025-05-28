package com.example.smartgarden;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetButton;
    private TextView backToLoginText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Referensi ke elemen UI
        emailInput = findViewById(R.id.fpsemail);
        resetButton = findViewById(R.id.fpsreset);
        backToLoginText = findViewById(R.id.backToLoginText); // Tambahkan ini

        // Aksi tombol reset password
        resetButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim permintaan reset password ke Firebase
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Link reset telah dikirim ke email kamu", Toast.LENGTH_LONG).show();
                            finish(); // Menutup activity, kembali ke login
                        } else {
                            String errorMsg = (task.getException() != null) ? task.getException().getMessage() : "Terjadi kesalahan";
                            Toast.makeText(ForgotPasswordActivity.this, "Gagal mengirim reset: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Aksi ketika "Masuk" ditekan
        backToLoginText.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Supaya user tidak bisa kembali ke halaman ini dengan tombol back
        });
    }
}
