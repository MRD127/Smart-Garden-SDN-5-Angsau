package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val daftarButton: Button = findViewById(R.id.rgsmasuk)
        val masukText: TextView = findViewById(R.id.rgsregister)
        val passwordField: EditText = findViewById(R.id.rgspassword)
        val confirmPasswordField: EditText = findViewById(R.id.rgsconfirm)

        var isPasswordVisible = false
        var isConfirmPasswordVisible = false

        daftarButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        masukText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Fungsi untuk menampilkan atau menyembunyikan password
        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordField.compoundDrawablesRelative[2] // Ambil ikon mata
                if (drawableEnd != null && event.rawX >= (passwordField.right - drawableEnd.bounds.width())) {
                    isPasswordVisible = !isPasswordVisible
                    togglePasswordVisibility(passwordField, isPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }

        confirmPasswordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = confirmPasswordField.compoundDrawablesRelative[2] // Ambil ikon mata
                if (drawableEnd != null && event.rawX >= (confirmPasswordField.right - drawableEnd.bounds.width())) {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                    togglePasswordVisibility(confirmPasswordField, isConfirmPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    // Fungsi untuk mengubah tipe input dari password
    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        editText.inputType = if (isVisible) {
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text.length) // Agar kursor tetap di akhir teks
    }
}
