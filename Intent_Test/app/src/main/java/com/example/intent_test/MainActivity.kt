package com.example.intent_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun gogogo(view: View)
    {
        val put_rss_url = findViewById<EditText>(R.id.put_rss_url)
        var str : String = put_rss_url.text.toString()
        val intent = Intent(this, ForTest::class.java)  // 인텐트를 생성해줌,
        // or val intent = Intent(this@MainActivity, SubActivity::class.java)
        //자기자신 클래스의 정보와 띄워줄 화면의 정보를 적어준다.
        intent.putExtra("msg", str)
        // (key, value) 전달할 정보가 없다면 이 문장을 사용하지 않아도 됨.
        // 전달할 정보가 많다면 더 적어줄 수도 있다.
        startActivity(intent)  // 화면 전환을 시켜줌
        finish()
    }
}