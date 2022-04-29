package com.nyanyajaguar.a10_weather

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.longToast
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val url = "http://api.openweathermap.org/data/2.5/weather?"+
            "q=seoul&units=metric&appid=55b10a8cdd70d63663cafb436dacf13f"

    fun get_weather(view : View) {

        doAsync {

            val result = URL(url).readText()
            uiThread {
                Log.d("json",result)
                longToast("Request performed")

                var json = JSONObject(result)

                val city = json.getString("name")
                val country = json.getJSONObject("sys").getString("country")
                val temp = json.getJSONObject("main").getString("temp")
                val weather = json.getJSONArray("weather")
                        .getJSONObject(0).getString("description")
                val wind = json.getJSONObject("wind").getString("speed")

                text_city.text = city
                text_country.text = country
                text_temp.text = temp
                text_weather.text = weather
                text_wind.text = wind

            }

        }

    }






}
