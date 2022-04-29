package com.nyanyajaguar.a05_layout

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_sms.*

class sms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
    }

    fun send_sms(view: View){
        var sms_intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:01049403347"))
        sms_intent.putExtra("sms_body",editText2.text.toString())
        startActivity(sms_intent)
    }


}
