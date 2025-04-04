package com.example.smartgarden

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

// Tandai menu "detail" sebagai aktif
        bottomNav.selectedItemId = R.id.setting
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
                    overridePendingTransition(0, 0) // Jika ada fragment detail, bisa dipanggil di sini
                    true
                }
                R.id.history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.setting -> {

                    true
                }
                else -> false
            }
        }
        }
    }
