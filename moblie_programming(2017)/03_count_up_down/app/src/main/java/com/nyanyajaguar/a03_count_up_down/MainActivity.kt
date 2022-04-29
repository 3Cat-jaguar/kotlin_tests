package com.nyanyajaguar.a03_count_up_down

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun button_up(view: View) {
        number = number + 1
        textView.text = "" + number
    }

    fun button_down(view:View) {
        number = number - 1
        textView.text = "" + number
    }

}
