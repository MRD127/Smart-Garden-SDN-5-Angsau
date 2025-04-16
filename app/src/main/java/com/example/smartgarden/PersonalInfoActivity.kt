package com.example.smartgarden

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PersonalInfoActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnBack: ImageButton
    private lateinit var profileImageView: ImageView
    private lateinit var btnChangeProfilePicture: Button

    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val IMAGE_REQUEST_CODE = 1000
    private val PROFILE_IMAGE_NAME = "profile_picture.png"  // Nama file untuk foto profil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        // Inisialisasi komponen UI
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnBack = findViewById(R.id.btnBack)
        profileImageView = findViewById(R.id.profileImage)
        btnChangeProfilePicture = findViewById(R.id.btnChangeProfilePicture)

        // Menambahkan aksi tombol kembali
        btnBack.setOnClickListener {
            onBackPressed()  // Fungsi untuk kembali ke activity sebelumnya
        }

        // Menambahkan aksi tombol untuk memilih foto
        btnChangeProfilePicture.setOnClickListener {
            openImagePicker()  // Fungsi untuk memilih foto
        }

        // Mengecek apakah pengguna sudah login
        if (currentUser != null) {
            loadUserProfile(currentUser.uid)
        } else {
            Toast.makeText(this, "Pengguna tidak login", Toast.LENGTH_SHORT).show()
        }

        // Memuat foto profil dari penyimpanan lokal (jika ada)
        loadProfileImage()
    }

    // Fungsi untuk memuat profil pengguna dari Firestore
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

    // Fungsi untuk membuka galeri atau kamera
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    // Menangani hasil dari pemilihan foto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_REQUEST_CODE) {
            val selectedImage = data?.data
            selectedImage?.let {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                    profileImageView.setImageBitmap(bitmap)  // Menampilkan gambar pada ImageView
                    saveProfileImage(bitmap)  // Menyimpan gambar ke penyimpanan lokal
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk menyimpan gambar profil ke penyimpanan lokal
    private fun saveProfileImage(bitmap: Bitmap) {
        val file = File(filesDir, PROFILE_IMAGE_NAME)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            Toast.makeText(this, "Foto profil berhasil disimpan", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal menyimpan foto profil", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk memuat gambar profil dari penyimpanan lokal
    private fun loadProfileImage() {
        val file = File(filesDir, PROFILE_IMAGE_NAME)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            profileImageView.setImageBitmap(bitmap)  // Menampilkan gambar yang disimpan
        }
    }
}
