package com.nyanyajaguar.a03_2_calculate_cross

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var number1 = 0
    var number2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun button_up1(view: View) {
        textView3.text = ""
        number1 = number1 + 1
        textView1.text = "" + number1
    }

    fun button_down1(view: View) {
        textView3.text = ""
        number1 = number1 - 1
        textView1.text = "" + number1
    }

    fun button_up2(view: View) {
        textView3.text = ""
        number2 = number2 + 1
        textView2.text = "" + number2
    }

    fun button_down2(view: View) {
        textView3.text = ""
        number2 = number2 - 1
        textView2.text = "" + number2
    }

    fun button_cal(view:View) {
        textView3.text = "" + number1 * number2
    }

}