package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val daftarButton: Button = findViewById(R.id.rgsmasuk)
        val masukText: TextView = findViewById(R.id.rgsregister)
        val nameField: EditText = findViewById(R.id.rgsusername)
        val emailField: EditText = findViewById(R.id.rgsemail)
        val passwordField: EditText = findViewById(R.id.rgspassword)
        val confirmPasswordField: EditText = findViewById(R.id.rgsconfirm)

        var isPasswordVisible = false
        var isConfirmPasswordVisible = false

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        daftarButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()
            val confirmPassword = confirmPasswordField.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            // Simpan display name ke Firebase Auth (optional)
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(profileUpdates)

                            // Simpan ke Firestore
                            val userData = hashMapOf(
                                "uid" to user?.uid,
                                "name" to name,
                                "email" to email
                            )

                            firestore.collection("users")
                                .document(user!!.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "Gagal simpan data: ${e.message}", Toast.LENGTH_SHORT).show()
                                }

                        } else {
                            Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        masukText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        passwordField.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordField.compoundDrawablesRelative[2]
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
                val drawableEnd = confirmPasswordField.compoundDrawablesRelative[2]
                if (drawableEnd != null && event.rawX >= (confirmPasswordField.right - drawableEnd.bounds.width())) {
                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                    togglePasswordVisibility(confirmPasswordField, isConfirmPasswordVisible)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        editText.inputType = if (isVisible) {
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text.length)
    }
}
