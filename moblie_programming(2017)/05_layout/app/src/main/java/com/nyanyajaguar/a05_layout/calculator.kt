package com.nyanyajaguar.a05_layout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_calculator.*

class calculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
    }


    var number = 0


    fun add(view: View) {
        var n = editText.text.toString().toInt()
        number = number + n
        textView.text = "" + number
    }

    fun clear(view: View) {
        var n = editText.text.toString().toInt()
        number = 0
        textView.text = "" + number
    }

}
