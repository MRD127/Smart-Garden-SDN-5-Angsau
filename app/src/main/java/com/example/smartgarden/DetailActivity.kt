package com.example.smartgarden

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.github.anastr.speedviewlib.SpeedView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {
    private val API_KEY = "47730936a35a0e34ea8ecbf7e9945d19" // Ganti dengan API Key Anda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Tandai menu "detail" sebagai aktif
        bottomNav.selectedItemId = R.id.detail

        // Listener untuk BottomNavigationView
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    overridePendingTransition(0, 0) // Agar tidak menumpuk activity
                    true
                }

                R.id.detail -> {
                    // Sudah di halaman detail, tidak perlu apa-apa
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

        // Ambil data cuaca ketika activity ini dibuka
        fetchWeatherData("Pelaihari") // Ganti dengan kota yang diinginkan
    }

    // Fungsi untuk mengambil data cuaca
    private fun fetchWeatherData(city: String) {
        val textCity = findViewById<TextView>(R.id.textCity)
        val textTemp = findViewById<TextView>(R.id.textTemp)
        val textDesc = findViewById<TextView>(R.id.textDesc)
        val imageWeather = findViewById<ImageView>(R.id.imageWeather)
        val speedView = findViewById<SpeedView>(R.id.speedView)

        val poppinsFont: Typeface? = ResourcesCompat.getFont(this, R.font.poppins_bold)



        // Tes nilai (misal suhu)
        speedView.speedTo(40f)


        // Memanggil API cuaca menggunakan Retrofit
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
                    Toast.makeText(this@DetailActivity, "Gagal mengambil data cuaca", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}