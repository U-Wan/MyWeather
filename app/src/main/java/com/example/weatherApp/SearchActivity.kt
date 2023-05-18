package com.example.weatherApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherApp.databinding.ActivitySearchBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val url = "https://api.openweathermap.org/data/2.5/weather"
    private val appid = "ad4b8c1c1a3315fdd473ce4b31bc5258"

    var df: DecimalFormat = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.btnGet.setOnClickListener{ view->
            getWeatherDetails()
        }
    }

    fun getWeatherDetails() {
        val tempUrl: String
        val city: String = binding.etCity.text.toString().trim()
        if (city == "") {
            binding.tvResult.text = "City field can not be empty!"
        } else {
            tempUrl ="$url?q=$city&appid=$appid"
            val stringRequest =
                StringRequest(Request.Method.POST, tempUrl,
                    { response ->
                        var output = ""
                        try {
                            val jsonResponse = JSONObject(response)
                            val jsonArray = jsonResponse.getJSONArray("weather")
                            val jsonObjectWeather = jsonArray.getJSONObject(0)
                            val description = jsonObjectWeather.getString("description")
                            val jsonObjectMain = jsonResponse.getJSONObject("main")
                            val temp = jsonObjectMain.getDouble("temp") - 273.15
                            val pressure = jsonObjectMain.getInt("pressure").toFloat()
                            val humidity = jsonObjectMain.getInt("humidity")
                            val jsonObjectWind = jsonResponse.getJSONObject("wind")
                            val wind = jsonObjectWind.getString("speed")
                            val jsonObjectClouds = jsonResponse.getJSONObject("clouds")
                            val clouds = jsonObjectClouds.getString("all")
                            val cityName = jsonResponse.getString("name")
                            binding.txtCityname.text="current weather in $cityName: "
                            output += """
                                |Temp: ${df.format(temp)}  
                                |Humidity: $humidity% 
                                |Description: $description 
                                |Cloudiness: $clouds% 
                                |Wind Speed: ${wind}m/s
                                |Pressure: $pressure hPa""".trimMargin()
                            binding.tvResult.text = output
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }) { error ->
                    Toast.makeText(
                        applicationContext,
                        error.toString().trim(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            val requestQueue: RequestQueue = Volley.newRequestQueue(applicationContext)
            requestQueue.add(stringRequest)
        }
    }
}