package com.example.weatherApp.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherApp.R
import com.example.weatherApp.databinding.FragmentFirstBinding
import com.example.weatherApp.databinding.FragmentThirdBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat


class ThirdFragment : Fragment() {
    var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    var windSpeed: TextView? = null
    var weatherState:TextView? = null
    var Temperature:TextView? = null
    var mweatherIcon: ImageView? = null
    var town:String?=null



    private val url = "https://api.openweathermap.org/data/2.5/weather"
    private val appid = "a29a88bea107cebcba998e2f8a3c15c9"
    var df: DecimalFormat = DecimalFormat("#.##")

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherState=binding.weatherCondition
        Temperature=binding.temperature
        mweatherIcon=binding.weatherIcon
        windSpeed=binding.windSpeed

        val prefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        town= prefs.getString("town_3", "") ?: ""

        getWeatherDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getWeatherDetails() {
        var tempUrl = ""
        val city = town.toString()
        tempUrl ="$url?q=$city&appid=$appid"
        val stringRequest =
            StringRequest(
                Request.Method.POST, tempUrl,
                { response ->
                    try {
                        val jsonResponse = JSONObject(response)
                        val jsonArray = jsonResponse.getJSONArray("weather")
                        val jsonObjectWeather = jsonArray.getJSONObject(0)
                        val description = jsonObjectWeather.getString("description")
                        val jsonObjectMain = jsonResponse.getJSONObject("main")
                        val mCondition: Int =jsonResponse.getJSONArray("weather").getJSONObject(0).getInt("id");
                        val temp = jsonObjectMain.getDouble("temp") - 273.15
                        val jsonObjectWind = jsonResponse.getJSONObject("wind")
                        val wind = jsonObjectWind.getString("speed")

                        Temperature?.text ="${df.format(temp)} °C"
                        windSpeed?.text = "Wind Speed: ${wind}m/s"
                        weatherState?.text = description

                        val resourceName= updateWeatherIcon(condition = mCondition)
                        val resourceId = resources.getIdentifier(resourceName, "drawable", context?.packageName)
                        mweatherIcon?.setImageResource(resourceId)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }) { error ->
                Toast.makeText(
                    activity?.applicationContext,
                    error.toString().trim(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(activity?.applicationContext)
        requestQueue.add(stringRequest)
    }

    private fun updateWeatherIcon(condition: Int): String? {
        if (condition in 0..300) {
            return "thunderstrom1"
        } else if (condition in 300..500) {
            return "lightrain"
        } else if (condition in 500..600) {
            return "shower"
        } else if (condition in 600..700) {
            return "snow2"
        } else if (condition in 701..800) {
            return "sunny"
        } else if (condition in 801..804) {
            return "cloudy"
        } else if (condition in 900..902) {
            return "thunderstrom1"
        }
        if (condition == 903) {
            return "snow1"
        }
        if (condition == 904) {
            return "sunny"
        }
        return if (condition in 905..1000) {
            "thunderstrom2"
        } else "fog"
    }



}

