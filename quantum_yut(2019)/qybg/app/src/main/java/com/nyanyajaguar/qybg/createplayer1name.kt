package com.nyanyajaguar.qybg

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.nyanyajaguar.qybg.R
import kotlinx.android.synthetic.main.activity_player1.*

var playername1 : String = ""

class createplayer1name : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player1)

    }

    fun save1(view: View){

        playername1 = name1.text.toString()
        var intent = Intent(this, createplayer2name :: class.java)
        startActivity(intent)
        //finish()

    }

}
