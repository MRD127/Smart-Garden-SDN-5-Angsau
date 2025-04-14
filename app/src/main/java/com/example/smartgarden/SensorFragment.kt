package com.example.smartgarden

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

class SensorFragment : Fragment() {

    // Define TextViews
    private lateinit var intensitasTextView: TextView
    private lateinit var kelembabanTextView: TextView

    // Handler and Runnable for periodic updates
    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            // Update the sensor data
            updateSensorData()

            // Post the Runnable to execute again after 1 second (1000ms)
            handler.postDelayed(this, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Handle any arguments if needed
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_sensor, container, false)

        // Initialize the TextViews
        intensitasTextView = rootView.findViewById(R.id.intensitas)
        kelembabanTextView = rootView.findViewById(R.id.kelembaban)

        // Start the periodic updates
        handler.post(updateRunnable)

        return rootView
    }

    // Function to update sensor data with random values
    private fun updateSensorData() {
        // Generate random values for intensitas and kelembaban
        val randomIntensitas = Random.nextInt(54, 60)  // Random value between 0 and 100
        val randomKelembaban = Random.nextInt(46, 50)   // Random value between 0 and 100

        // Set the random values to the TextViews
        intensitasTextView.text = "${randomIntensitas}°"
        kelembabanTextView.text = "${randomKelembaban}%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the Runnable to prevent memory leaks when the fragment is destroyed
        handler.removeCallbacks(updateRunnable)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SensorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
