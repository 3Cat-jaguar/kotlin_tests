package com.example.intent_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ForTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_test)

        if(intent.hasExtra("msg")) {  //msg라는 키값을 가진 intent가 정보를 가지고 있다면 실행
            val result = findViewById<TextView>(R.id.result)
            result.text = intent.getStringExtra("msg")
            // text_sub의 문구를 msg라는 키값을 가진 intent의 정보로 바꾸어준다.
        }
    }
}