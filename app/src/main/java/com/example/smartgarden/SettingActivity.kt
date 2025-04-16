package com.example.smartgarden

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.setting

        // ✅ Menampilkan nama pengguna di bawah foto profil
        val tvUserName = findViewById<TextView>(R.id.profileName)
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        tvUserName.text = name ?: "Nama tidak tersedia"
                    } else {
                        tvUserName.text = "Data tidak ditemukan"
                    }
                }
                .addOnFailureListener {
                    tvUserName.text = "Gagal memuat nama"
                }
        }

        // ✅ Navigasi bottom bar
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.detail -> {
                    startActivity(Intent(this, DetailActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.setting -> true
                else -> false
            }
        }

        // ✅ Logout
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        logoutButton.setOnClickListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LandingpageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // ✅ Navigasi ke tiap setting item
        findViewById<LinearLayout>(R.id.personalInfo).setOnClickListener {
            startActivity(Intent(this, PersonalInfoActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.notifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.help).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.about).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // ✅ Set judul dan deskripsi tiap item
        setupSettingItem(R.id.personalInfo, R.drawable.ic_profile, "Personal Information", "Your profile information")
        setupSettingItem(R.id.notifications, R.drawable.ic_notifications, "Notifications", "Notification settings")
        setupSettingItem(R.id.help, R.drawable.ic_help, "Help", "Data preferences and storage settings")
        setupSettingItem(R.id.about, R.drawable.ic_info, "About", "Version 1.0")

        // ✅ Tampilkan gambar profil saat pertama kali
        loadProfileImage()
    }

    private fun setupSettingItem(itemId: Int, iconRes: Int, title: String, desc: String) {
        val itemView = findViewById<LinearLayout>(itemId)
        itemView.findViewById<TextView>(R.id.settingTitle).text = title
        itemView.findViewById<TextView>(R.id.settingDesc).text = desc
        itemView.findViewById<View>(R.id.settingIcon).setBackgroundResource(iconRes)
    }

    // ✅ Fungsi untuk memuat gambar profil dari penyimpanan lokal (jika ada)
    private fun loadProfileImage() {
        val file = File(filesDir, "profile_picture.png")
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val profileImageView = findViewById<ImageView>(R.id.profileImage)
            profileImageView.setImageBitmap(bitmap)
        }
    }

    // ✅ Agar gambar diperbarui setiap kembali ke halaman Setting
    override fun onResume() {
        super.onResume()
        loadProfileImage()
    }
}
