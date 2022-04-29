package com.nyanyajaguar.weather_5day

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


    val url = "http://api.openweathermap.org/data/2.5/forecast/daily?"+
            "q=Seoul&units=metric&cnt=5&appid=6af8dad4784c634d3674f60110f2a015"

    fun get_weather(view : View) {

        doAsync {

            val result = URL(url).readText()
            uiThread {
                Log.d("json",result)
                longToast("Request performed")

                var json = JSONObject(result)

                val city = json.getJSONObject("city").getString("name")
                val country = json.getJSONObject("city").getString("country")

                text_city.text = city
                text_country.text = country


                val temp0_day = json.getJSONArray("list").getJSONObject(0).getJSONObject("temp").getString("day")
                val temp0_min = json.getJSONArray("list").getJSONObject(0).getJSONObject("temp").getString("min")
                val temp0_max = json.getJSONArray("list").getJSONObject(0).getJSONObject("temp").getString("max")
                val weather0 = json.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main")
                val wind0 = json.getJSONArray("list").getJSONObject(0).getString("speed")

                day0_temp.text = "" + temp0_day + "°C  (" + temp0_min + "/" + temp0_max + ")"
                day0_weather.text = weather0
                day0_wind.text = wind0


                val temp1_day = json.getJSONArray("list").getJSONObject(1).getJSONObject("temp").getString("day")
                val temp1_min = json.getJSONArray("list").getJSONObject(1).getJSONObject("temp").getString("min")
                val temp1_max = json.getJSONArray("list").getJSONObject(1).getJSONObject("temp").getString("max")
                val weather1 = json.getJSONArray("list").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("main")
                val wind1 = json.getJSONArray("list").getJSONObject(1).getString("speed")

                day1_temp.text = "" + temp1_day + "°C (" + temp1_min + "/" + temp1_max + ")"
                day1_weather.text = weather1
                day1_wind.text = wind1


                val temp2_day = json.getJSONArray("list").getJSONObject(2).getJSONObject("temp").getString("day")
                val temp2_min = json.getJSONArray("list").getJSONObject(2).getJSONObject("temp").getString("min")
                val temp2_max = json.getJSONArray("list").getJSONObject(2).getJSONObject("temp").getString("max")
                val weather2 = json.getJSONArray("list").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("main")
                val wind2 = json.getJSONArray("list").getJSONObject(2).getString("speed")

                day2_temp.text = "" + temp2_day + "°C (" + temp2_min + "/" + temp2_max + ")"
                day2_weather.text = weather2
                day2_wind.text = wind2


                val temp3_day = json.getJSONArray("list").getJSONObject(3).getJSONObject("temp").getString("day")
                val temp3_min = json.getJSONArray("list").getJSONObject(3).getJSONObject("temp").getString("min")
                val temp3_max = json.getJSONArray("list").getJSONObject(3).getJSONObject("temp").getString("max")
                val weather3 = json.getJSONArray("list").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("main")
                val wind3 = json.getJSONArray("list").getJSONObject(3).getString("speed")

                day3_temp.text = "" + temp3_day + "°C (" + temp3_min + "/" + temp3_max + ")"
                day3_weather.text = weather3
                day3_wind.text = wind3


                val temp4_day = json.getJSONArray("list").getJSONObject(4).getJSONObject("temp").getString("day")
                val temp4_min = json.getJSONArray("list").getJSONObject(4).getJSONObject("temp").getString("min")
                val temp4_max = json.getJSONArray("list").getJSONObject(4).getJSONObject("temp").getString("max")
                val weather4 = json.getJSONArray("list").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getString("main")
                val wind4 = json.getJSONArray("list").getJSONObject(4).getString("speed")

                day4_temp.text = "" + temp4_day + "°C (" + temp4_min + "/" + temp4_max + ")"
                day4_weather.text = weather4
                day4_wind.text = wind4



            }

        }

    }






}
