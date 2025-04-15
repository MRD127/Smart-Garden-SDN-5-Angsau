package com.example.smartgarden

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info) // Pastikan ini nama file XML kamu

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)

        if (currentUser != null) {
            loadUserProfile(currentUser.uid)
        } else {
            Toast.makeText(this, "Pengguna tidak login", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserProfile(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name")
                    val email = document.getString("email")

                    etName.setText(name ?: "Tidak tersedia")
                    etEmail.setText(email ?: "Tidak tersedia")
                } else {
                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal memuat profil", Toast.LENGTH_SHORT).show()
            }
    }
}