package com.example.smartgarden;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private Button resetButton;
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
                            finish(); // Kembali ke halaman sebelumnya (misalnya login)
                        } else {
                            String errorMsg = (task.getException() != null) ? task.getException().getMessage() : "Terjadi kesalahan";
                            Toast.makeText(ForgotPasswordActivity.this, "Gagal mengirim reset: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}