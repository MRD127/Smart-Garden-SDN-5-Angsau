package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Referensi ke elemen UI
        val emailField = findViewById<EditText>(R.id.lgnusername)
        val passwordField = findViewById<EditText>(R.id.lgnpassword)
        val loginButton = findViewById<Button>(R.id.lgnmasuk)
        val registerText = findViewById<TextView>(R.id.lgndaftar2)
        val forgotPasswordText = findViewById<TextView>(R.id.lgnlupapassword)

        // Navigasi ke halaman lupa password
        forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke halaman RegisterActivity saat teks "Daftar" diklik
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Fungsi login saat tombol "Masuk" diklik
        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Logika untuk menampilkan atau menyembunyikan kata sandi
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Posisi drawableEnd (ikon mata)
                val drawable = passwordField.compoundDrawables[drawableEnd]

                if (drawable != null && event.rawX >= (passwordField.right - drawable.bounds.width() - passwordField.paddingEnd)) {
                    if (isPasswordVisible) {
                        passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_transparent, 0)
                    } else {
                        passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
                    }
                    passwordField.typeface = passwordField.typeface
                    passwordField.setSelection(passwordField.text.length)
                    isPasswordVisible = !isPasswordVisible
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}
