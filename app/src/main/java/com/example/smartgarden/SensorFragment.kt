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

    private lateinit var database: DatabaseReference
    private lateinit var soilMoistureText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)

        soilMoistureText = view.findViewById(R.id.textSoilMoisture)

        database = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("sensor/soil_moisture")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val soilMoisture = snapshot.getValue(Int::class.java)
                if (soilMoisture != null) {
                    soilMoistureText.text = "$soilMoisture%"
                    SensorData.setSoilMoisture(soilMoisture) // broadcast ke DetailActivity
                } else {
                    soilMoistureText.text = "Data tidak tersedia"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                soilMoistureText.text = "Gagal memuat data sensor"
            }
        })

        return view
    }
}
