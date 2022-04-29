package com.nyanyajaguar.a08_dice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun roll(view : View) {
        var n1 = (Math.random() * 6) + 1
        var n2 = n1.toInt()

        var n3 = (Math.random() * 6) + 1
        var n4 = n3.toInt()

        dice1.set_number(n2)
        dice2.set_number(n4)

        textView.text = "" + (n2+n4)
    }

}
