package com.example.smartgarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var resetButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referensi ke elemen UI
        emailInput = findViewById(R.id.fpsemail)
        resetButton = findViewById(R.id.fpsreset)

        resetButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim permintaan reset password ke Firebase
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Link reset telah dikirim ke email kamu", Toast.LENGTH_LONG).show()
                        finish() // Kembali ke halaman sebelumnya (misalnya login)
                    } else {
                        val errorMsg = task.exception?.message ?: "Terjadi kesalahan"
                        Toast.makeText(this, "Gagal mengirim reset: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
