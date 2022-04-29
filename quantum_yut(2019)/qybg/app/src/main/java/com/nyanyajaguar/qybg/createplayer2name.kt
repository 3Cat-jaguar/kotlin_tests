package com.nyanyajaguar.qybg

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nyanyajaguar.qybg.MainActivity
import com.nyanyajaguar.qybg.R
import kotlinx.android.synthetic.main.activity_player2.*

var playername2 : String = ""

class createplayer2name : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player2)
    }

    fun save2(view: View){

        playername2 = name2.text.toString()
        var intent = Intent(this, MainActivity :: class.java)
        startActivity(intent)
        //finish()

    }

}
