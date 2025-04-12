package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PersonalInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)

        val btnEdit = findViewById<Button>(R.id.btnEditProfile)
        btnEdit.setOnClickListener {
            // Navigasi ke EditProfileActivity
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
