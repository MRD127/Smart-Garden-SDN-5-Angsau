package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referensi ke elemen UI
        val passwordField = findViewById<EditText>(R.id.lgnpassword)
        val loginButton = findViewById<Button>(R.id.lgnmasuk)
        val registerText = findViewById<TextView>(R.id.lgndaftar2)

        // Navigasi ke halaman MainActivity saat tombol "Masuk" diklik
        loginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Navigasi ke halaman RegisterActivity saat teks "Daftar" diklik
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Logika untuk menampilkan atau menyembunyikan kata sandi
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // Posisi drawableEnd (ikon mata)
                val drawable = passwordField.compoundDrawables[drawableEnd]

                if (drawable != null && event.rawX >= (passwordField.right - drawable.bounds.width() - passwordField.paddingEnd)) {
                    if (isPasswordVisible) {
                        // Sembunyikan password
                        passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_transparent, 0)
                    } else {
                        // Tampilkan password
                        passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        passwordField.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0)
                    }

                    // **Tambahan penting untuk mencegah bug:**
                    passwordField.typeface = passwordField.typeface // Mencegah tampilan teks berubah
                    passwordField.setSelection(passwordField.text.length) // Menjaga posisi kursor tetap di akhir teks

                    // Toggle status visibility
                    isPasswordVisible = !isPasswordVisible
                    return@setOnTouchListener true
                }
            }
            false
        }

    }
}
