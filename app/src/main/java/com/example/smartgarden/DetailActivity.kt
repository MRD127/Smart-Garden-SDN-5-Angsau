package com.example.smartgarden

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.github.anastr.speedviewlib.SpeedView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private val API_KEY = "47730936a35a0e34ea8ecbf7e9945d19"
    private lateinit var barCahaya: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var speedView: SpeedView
    private lateinit var database: DatabaseReference
    private lateinit var humidityText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var barHumidity: ProgressBar
    private lateinit var barTemperatur: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        // Inisialisasi view
        barCahaya = findViewById(R.id.barCahaya)
        percentageText = findViewById(R.id.percentageText)
        speedView = findViewById(R.id.speedView)
        barHumidity = findViewById(R.id.barHumidity)
        humidityText = findViewById(R.id.percentageHumidity)
        barTemperatur = findViewById(R.id.barTemperature)
        temperatureText = findViewById(R.id.percentageTemperature)

        setupBottomNav()
        fetchWeatherData("Pelaihari")

        // Update SpeedView dari SensorData secara real-time
        SensorData.observe { moisture ->
            speedView.speedTo(moisture.toFloat())


        }

        // Update SpeedView dari SensorData secara real-time
        SensorHumidity.observe { humidity ->
            barHumidity


        }

        // Firebase untuk humidity
        val humidityRef = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("sensor/humidity")

        humidityRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val humidity = snapshot.getValue(Int::class.java)
                if (humidity != null) {
                    humidityText.text = "$humidity%"
                    barHumidity.progress = humidity
                    SensorHumidity.setHumidity(humidity.toFloat())
                } else {
                    humidityText.text = "Data tidak tersedia"
                    barHumidity.progress = 0
                }
            }

            override fun onCancelled(error: DatabaseError) {
                humidityText.text = "Gagal memuat data humidity"
                barHumidity.progress = 0
            }
        })

        // Firebase untuk humidity
        val temperatureRef = FirebaseDatabase.getInstance("https://smart-garden-sdn-5-angsau-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("sensor/temperature")

        temperatureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temperature = snapshot.getValue(Int::class.java)
                if (temperature != null) {
                    temperatureText.text = "$temperature%"
                    barTemperatur.progress = temperature
                } else {
                    temperatureText.text = "Data tidak tersedia"
                    barTemperatur.progress = 0
                }
            }

            override fun onCancelled(error: DatabaseError) {
                temperatureText.text = "Gagal memuat data humidity"
                barTemperatur.progress = 0
            }
        })


    }


    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.selectedItemId = R.id.detail

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.detail -> true
                R.id.history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.setting -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    private fun updateProgress(value: Int) {
        barCahaya.progress = value
        percentageText.text = "$value%"
    }

    override fun onDestroy() {
        super.onDestroy()
        SensorData.removeObservers()
    }

    private fun fetchWeatherData(city: String) {
        val textCity = findViewById<TextView>(R.id.textCity)
        val textTemp = findViewById<TextView>(R.id.textTemp)
        val textDesc = findViewById<TextView>(R.id.textDesc)
        val imageWeather = findViewById<ImageView>(R.id.imageWeather)

        val poppinsFont: Typeface? = ResourcesCompat.getFont(this, R.font.poppins_bold)

        ApiClient.instance.getWeather(city, API_KEY).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    textCity.text = weather?.name
                    textTemp.text = "${weather?.main?.temp}°C"
                    textDesc.text = weather?.weather?.get(0)?.description?.replaceFirstChar { it.uppercaseChar() }

                    val iconUrl = "https://openweathermap.org/img/wn/${weather?.weather?.get(0)?.icon}@2x.png"
                    Picasso.get().load(iconUrl).into(imageWeather)
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data cuaca", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
