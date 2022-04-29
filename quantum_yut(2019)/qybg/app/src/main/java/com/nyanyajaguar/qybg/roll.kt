package com.nyanyajaguar.qybg

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class roll : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roll)
    }


    fun play(view: View){

        var intent = Intent(this, createplayer1name :: class.java)
        startActivity(intent)

    }


}
