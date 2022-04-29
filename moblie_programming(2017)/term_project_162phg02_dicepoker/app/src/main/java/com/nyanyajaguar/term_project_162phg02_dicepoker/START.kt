package com.nyanyajaguar.term_project_162phg02_dicepoker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_start.*

var name1 = 1
var name2 = 2

var notice = 0


var name_1: String = ""
var name_2: String = ""


class START : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    fun go_game(view: View) {
        if(notice == 0) {

            if(name_1 == "" && name_2 == "") {
            Toast.makeText(this, "player1, player2 의 이름을 지정해주세요", Toast.LENGTH_SHORT).show()
            }

            else if(name_1 == "" && name_2 != ""){
            Toast.makeText(this, "player1의 이름을 지정해주세요", Toast.LENGTH_SHORT).show()
            }

            else if(name_2 == "" && name_1 != ""){
            Toast.makeText(this, "player2 의 이름을 지정해주세요", Toast.LENGTH_SHORT).show()
            }

            else if(name_1 != "" && name_2 != "") {
            var intent = Intent(this, GAME :: class.java)
            startActivity(intent)
            }

        }
    }

    fun go_new(view: View) {
        if(notice == 0){
        var intent = Intent(this, NEW :: class.java)
        startActivityForResult(intent,name1)
        }
    }


    fun go_new2(view: View) {
        if(notice == 0) {
        var intent = Intent(this, NEW2 :: class.java)
        startActivityForResult(intent,name2)
        }
    }

    fun notice_in(view:View) {
        start_notice.setVisibility(View.VISIBLE)
        start_notice.bringToFront()
        start_notice.invalidate()
        notice = notice + 1
    }

    fun notice_out(view:View) {
        start_notice.setVisibility(View.GONE)
        notice = notice - 1
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent) {
        super.onActivityResult(requestCode, resultCode, data);

        when (resultCode) {
            name1 -> {
                name_1 = data.getStringExtra("name")
                p1_name.text = name_1
                Picasso.with(this).load(img_data).into(p1_pic)
            }
        }

        when (resultCode) {
            name2 -> {
                name_2 = data.getStringExtra("name")
                p2_name.text = name_2
                Picasso.with(this).load(img_data2).into(p2_pic)
            }
        }
    }



}
