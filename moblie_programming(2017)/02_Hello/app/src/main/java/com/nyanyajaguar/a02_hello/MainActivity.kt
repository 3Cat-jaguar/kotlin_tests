package com.nyanyajaguar.a02_hello

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun button1(view : View) {
        textView.text = "Hello, Ewha!"

    }
    fun button2(view : View) {
        textView.text = "Nice to meet you"
    }
    fun button3(view : View) {
        textView.text = "Hello World!"
    }


}
