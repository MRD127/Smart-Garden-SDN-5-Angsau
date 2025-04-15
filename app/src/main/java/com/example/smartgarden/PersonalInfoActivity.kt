package com.example.smartgarden

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.widget.ImageButton

class PersonalInfoActivity : AppCompatActivity() {

    private val IMAGE_PICK_CODE = 1000
    private val CAMERA_REQUEST_CODE = 1001
    private val PERMISSION_REQUEST_CODE = 1002
    private lateinit var profileImage: ImageView

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private var isEditing = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        profileImage = findViewById(R.id.profileImage)
        val btnChangeProfilePic = findViewById<ImageButton>(R.id.btnChangeProfilePic)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnEdit = findViewById<ImageButton>(R.id.btnEdit)
        btnSave = findViewById(R.id.btnSave)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)

        btnChangeProfilePic.setOnClickListener {
            showImagePickerDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }

        checkPermissions()

        // Awal: tidak bisa diedit
        setEditingEnabled(false)

        btnEdit.setOnClickListener {
            isEditing = !isEditing
            setEditingEnabled(isEditing)
            btnSave.visibility = if (isEditing) Button.VISIBLE else Button.GONE
        }

        btnSave.setOnClickListener {
            isEditing = false
            setEditingEnabled(false)
            btnSave.visibility = Button.GONE
            Toast.makeText(this, "Data disimpan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setEditingEnabled(enabled: Boolean) {
        etName.isFocusable = enabled
        etName.isFocusableInTouchMode = enabled
        etName.isEnabled = enabled
        etName.isCursorVisible = enabled

        etEmail.isFocusable = enabled
        etEmail.isFocusableInTouchMode = enabled
        etEmail.isEnabled = enabled
        etEmail.isCursorVisible = enabled
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri", "Batal")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ubah Foto Profil")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_PICK_CODE) {
                val imageUri = data.data
                profileImage.setImageURI(imageUri)
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                profileImage.setImageBitmap(imageBitmap)
            }
        }
    }

    private fun checkPermissions() {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        val permissionsNeeded = mutableListOf<String>()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

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
