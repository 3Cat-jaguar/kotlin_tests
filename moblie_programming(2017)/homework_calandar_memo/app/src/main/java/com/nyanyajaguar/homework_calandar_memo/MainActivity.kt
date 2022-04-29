package com.nyanyajaguar.homework_calandar_memo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_main.*

var ymd = ""

class MainActivity : AppCompatActivity(), DatePicker.OnDateChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datePicker.init(datePicker.year, datePicker.month, datePicker.dayOfMonth, this)
    }

    override fun onDateChanged(p0: DatePicker, y: Int, m: Int, d: Int) {

        ymd = "${datePicker.year}_${datePicker.month}_${datePicker.dayOfMonth}"

        var intent = Intent(this, Memo :: class.java)
        startActivity(intent)
    }

}
