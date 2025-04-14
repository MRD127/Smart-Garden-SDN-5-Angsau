package com.example.smartgarden

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private val listData = mutableListOf(
        HistoryItem("29-10-2025", "52%", "100%"),
        HistoryItem("22-10-2025", "61%", "50%"),
        HistoryItem("20-10-2025", "70%", "65%")
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        // Inisialisasi BottomNavigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.history

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
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.history -> true
                R.id.setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(listData) { item ->
            listData.remove(item)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }
}
