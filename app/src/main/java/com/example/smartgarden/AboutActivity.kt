package com.example.smartgarden

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var tvAppName: TextView
    private lateinit var tvAppVersion: TextView
    private lateinit var tvCopyright: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvAppName = findViewById(R.id.tvAppName)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        tvCopyright = findViewById(R.id.tvCopyright)

        tvAppName.text = "Smart Garden"
        tvAppVersion.text = "Versi 1.0"
        tvCopyright.text = "© 2025 Smart Garden Inc."

        // Inisialisasi tombol kembali
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Menutup activity dan kembali ke halaman sebelumnya
        }
    }
}
