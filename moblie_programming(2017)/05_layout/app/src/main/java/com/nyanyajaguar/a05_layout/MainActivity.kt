package com.nyanyajaguar.a05_layout

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_calculator.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun start_campus(view: View){
        var intent = Intent(this, campus :: class.java)
        startActivity(intent)
    }

    fun start_building(view: View){
        var intent = Intent(this, building :: class.java)
        startActivity(intent)
    }

    fun start_cal(view: View){
        var intent = Intent(this, calculator :: class.java)
        startActivity(intent)
    }

    fun start_sms(view: View){
        var intent = Intent(this, sms :: class.java)
        startActivity(intent)
    }



}
