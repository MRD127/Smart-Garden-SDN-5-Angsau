package com.example.smartgarden

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Mengambil reference untuk switch
        val notificationSwitch: Switch = findViewById(R.id.notificationSwitch)

        // Mengatur listener untuk switch
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Mengaktifkan notifikasi
                Toast.makeText(this, "Notifications Enabled", Toast.LENGTH_SHORT).show()
            } else {
                // Menonaktifkan notifikasi
                Toast.makeText(this, "Notifications Disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
