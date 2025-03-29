package com.example.smartgarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class LandingpageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landingpage) // Sesuaikan dengan layout yang digunakan

        val btnMulai = findViewById<Button>(R.id.lpmulai)

        btnMulai.setOnClickListener { // Pindah ke Activity Login
            val intent = Intent(
                this@LandingpageActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        }
    }
}
