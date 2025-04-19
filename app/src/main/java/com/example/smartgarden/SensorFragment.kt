package com.example.smartgarden

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.*

class SensorFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var soilMoistureText: TextView
    private lateinit var lightIntensityText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        // Inisialisasi TextView dari layout
        soilMoistureText = view.findViewById(R.id.textSoilMoisture)
        lightIntensityText = view.findViewById(R.id.intensitas)

        // Inisialisasi Firebase Realtime Database
        database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app")

        // Referensi kelembaban tanah
        val soilRef = database.getReference("sensor/soil_moisture")
        soilRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val soilMoisture = snapshot.getValue(Int::class.java)
                if (soilMoisture != null) {
                    soilMoistureText.text = "$soilMoisture%"
                    SensorData.setSoilMoisture(soilMoisture) // jika kamu butuh broadcast ke activity lain
                } else {
                    soilMoistureText.text = "Data tidak tersedia"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                soilMoistureText.text = "Gagal memuat data"
            }
        })

        // Referensi intensitas cahaya
        val lightRef = database.getReference("sensor/cahaya")
        lightRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val intensity = snapshot.getValue(Int::class.java)
                if (intensity != null) {
                    lightIntensityText.text = "$intensity Lux"
                } else {
                    lightIntensityText.text = "Data tidak tersedia"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                lightIntensityText.text = "Gagal memuat data"
            }
        })

        return view
    }
}
