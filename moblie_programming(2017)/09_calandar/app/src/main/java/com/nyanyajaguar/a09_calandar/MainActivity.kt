package com.nyanyajaguar.a09_calandar

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DatePicker.OnDateChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datePicker.init(datePicker.year, datePicker.month, datePicker.dayOfMonth, this)

    }

    override fun onDateChanged(view: DatePicker, y:Int, m:Int, d:Int) {

        var ymd = "${datePicker.year}_${datePicker.month}_${datePicker.dayOfMonth}"
        var pref = getSharedPreferences("diary", Context.MODE_PRIVATE)

        expense.setText(pref.getString(ymd+"e", ""))
        memo.setText(pref.getString(ymd+"m",""))
    }

    fun cmd_save(view: View) {

        var ymd = "${datePicker.year}_${datePicker.month}_${datePicker.dayOfMonth}"

        var pref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        var edit = pref.edit()

        edit.putString(ymd+"e", expense.text.toString())
        edit.putString(ymd+"m", memo.text.toString())

        edit.commit()
    }



}
