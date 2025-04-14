package com.example.smartgarden

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager

class PersonalInfoActivity : AppCompatActivity() {

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001
    private val PERMISSION_REQUEST_CODE = 1002
    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        // Mendapatkan reference ke ImageView untuk foto profil
        profileImage = findViewById(R.id.profileImage)

        // Tombol untuk mengubah foto profil
        val btnChangeProfilePic = findViewById<ImageButton>(R.id.btnChangeProfilePic)
        btnChangeProfilePic.setOnClickListener {
            // Menampilkan dialog untuk memilih foto dari galeri atau kamera
            showImagePickerDialog()
        }

        // Memeriksa izin jika aplikasi membutuhkan akses kamera atau penyimpanan
        checkPermissions()
    }

    // Menampilkan dialog untuk memilih antara mengambil foto atau memilih dari galeri
    private fun showImagePickerDialog() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri", "Batal")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ubah Foto Profil")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()  // Pilih untuk mengambil foto
                1 -> openGallery() // Pilih untuk memilih gambar dari galeri
                2 -> dialog.dismiss() // Batalkan
            }
        }
        builder.show()
    }

    // Memulai intent untuk membuka kamera
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    // Memulai intent untuk memilih gambar dari galeri
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Menangani hasil dari kamera atau galeri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_PICK_CODE) {
                // Jika memilih gambar dari galeri
                val imageUri = data.data
                profileImage.setImageURI(imageUri)
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                // Jika mengambil foto dari kamera
                val imageBitmap = data.extras?.get("data") as Bitmap
                profileImage.setImageBitmap(imageBitmap)
            }
        }
    }

    // Memeriksa dan meminta izin untuk mengakses kamera dan galeri
    private fun checkPermissions() {
        // Memeriksa izin kamera dan penyimpanan
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionsNeeded = mutableListOf<String>()

        // Jika izin belum diberikan, tambahkan ke daftar izin yang diperlukan
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // Jika ada izin yang perlu diminta, tampilkan permintaan izin
        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    // Menangani hasil permintaan izin
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin diberikan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Izin ditolak", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
