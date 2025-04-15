package com.example.smartgarden

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // (Opsional) Set judul di toolbar jika pakai ActionBar
        supportActionBar?.apply {
            title = "Bantuan"
            setDisplayHomeAsUpEnabled(true) // Tombol back
        }
    }

    // Fungsi ketika tombol back (panah) di toolbar ditekan
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
