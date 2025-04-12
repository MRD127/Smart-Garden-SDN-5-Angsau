package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etDob = findViewById<EditText>(R.id.etDob)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Optional: Pre-fill dengan data lama
        etName.setText("Muhammad Zidan")
        etEmail.setText("zidan@gmail.com")
        etPhone.setText("+62 812-3456-7890")
        etDob.setText("12 March 2000")
        etAddress.setText("Jl. Merdeka No. 45, Bandung")

        btnSave.setOnClickListener {
            // Simpan data - untuk sementara kita tampilkan toast
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()

            // Kembali ke halaman profil (PersonalInfoActivity)
            val intent = Intent(this, PersonalInfoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
