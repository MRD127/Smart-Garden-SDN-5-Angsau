package com.example.smartgarden

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat
import android.content.res.ColorStateList


class MainActivity : AppCompatActivity() {

    private val API_KEY = "47730936a35a0e34ea8ecbf7e9945d19" // Ganti dengan API Key OpenWeatherMap Anda
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityName = "Pelaihari" // Ganti dengan kota yang ingin dicari
        fetchWeatherData(cityName)
        
        val buttonSensor = findViewById<Button>(R.id.Button_sensor)
        val buttonAktuator = findViewById<Button>(R.id.Buton_akuator)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


// Tandai menu "detail" sebagai aktif
        bottomNav.selectedItemId = R.id.home
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                     // Sekarang benar-benar mengganti fragment
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
                    startActivity(Intent(this, SettingActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }


        // Default: Tampilkan SensorFragment saat pertama kali dibuka
        loadFragment(SensorFragment())

        buttonSensor.setOnClickListener {
            loadFragment(SensorFragment())
        }

        buttonAktuator.setOnClickListener {
            loadFragment(AktuatorFragment())
        }
    }


    private fun fetchWeatherData(city: String) {
        val textCity = findViewById<TextView>(R.id.textCity)
        val textTemp = findViewById<TextView>(R.id.textTemp)
        val textDesc = findViewById<TextView>(R.id.textDesc)
        val imageWeather = findViewById<ImageView>(R.id.imageWeather)

        ApiClient.instance.getWeather(city, API_KEY).enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    textCity.text = weather?.name
                    textTemp.text = "${weather?.main?.temp}°C"
                    textDesc.text = weather?.weather?.get(0)?.description?.capitalize()

                    val iconUrl = "https://openweathermap.org/img/wn/${weather?.weather?.get(0)?.icon}@2x.png"
                    Picasso.get().load(iconUrl).into(imageWeather)
                } else {
                    Toast.makeText(this@MainActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    }
