package com.nyanyajaguar.dicepoker_ver1

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_start.*


var notice = 0  // 도움말 - 닫힌상태 : 0 , 열린상태 : 1


var name_1: String = ""  // player1의 이름
var name_2: String = ""  // player2의 이름

var img_data : Uri? = null // player1의 사진
var img_data2 : Uri? = null // player2의 사진



class START : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    //player1 이미지 고르기
    fun select_photo1(view:View) {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,SELECT)
    }

    //player2 이미지 고르기
    fun select_photo2(view:View) {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,SELECT2)
    }


    // player 이미지 고른 데이터 정리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (requestCode == SELECT && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.data).into(p1_pic)
            img_data = data.data
        }

        if (requestCode == SELECT2 && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.data).into(p2_pic)
            img_data2 = data.data
        }

    }






    fun go_game(view: View) {

        // player 이름 입력한 값을 변수(String)로 인식
        name_1 = p1_name.text.toString()
        name_2 = p2_name.text.toString()

        // 게임 시작 전 player 이름이 다 입력됐는지 확인. 입력안됐으면 Toast 알림 띄우고, 입력돼있으면 게임화면으로 이동
        //도움말 열려있는 상태로는 진행 안됨(도움말 보다가 버튼 위치를 눌러도 작동 안하게끔)
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

    // 도움말 버튼 누르면 도움말 창 띄우고 notice 변수도 열린 상태(1)로 바꾸기
    fun notice_in(view:View) {
        start_notice.setVisibility(View.VISIBLE)
        start_notice.bringToFront()
        start_notice.invalidate()
        notice = notice + 1
    }

    // 도움창 닫기 버튼 누르면 창 사라지고 notice 변수도 닫힌 상태(0)로 바꾸기
    fun notice_out(view:View) {
        start_notice.setVisibility(View.GONE)
        notice = notice - 1
    }



}
