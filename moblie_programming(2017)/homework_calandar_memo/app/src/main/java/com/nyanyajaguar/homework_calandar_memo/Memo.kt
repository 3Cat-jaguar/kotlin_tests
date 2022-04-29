package com.nyanyajaguar.homework_calandar_memo

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_memo.*

class Memo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)

        var pref = getSharedPreferences("diary", Context.MODE_PRIVATE)

        what.setText(pref.getString(ymd+"w",""))
        expense.setText(pref.getString(ymd+"e", ""))
        memo.setText(pref.getString(ymd+"m",""))
    }

    fun cmd_save(view: View) {

        var pref = getSharedPreferences("diary", Context.MODE_PRIVATE)
        var edit = pref.edit()

        edit.putString(ymd+"w", what.text.toString())
        edit.putString(ymd+"e", expense.text.toString())
        edit.putString(ymd+"m", memo.text.toString())

        edit.commit()

        onBackPressed()


    }
}
