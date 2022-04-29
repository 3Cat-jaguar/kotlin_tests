package com.nyanyajaguar.term_project_162phg02_dicepoker


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new.*
import kotlinx.android.synthetic.main.activity_start.*


var img_data : Uri? = null

var newnotice = 0

class NEW : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)
    }

// 이미지 불러오는 코드

    fun select_photo(view: View) {
        if(newnotice == 0) {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data:Intent) {

        if(requestCode == SELECT && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.data).into(imageView)

            img_data = data.data
        }
    }

    fun new_notice_in(view:View) {
        new_notice.setVisibility(View.VISIBLE)
        editText.setClickable(false)
        new_notice.bringToFront()
        new_notice.invalidate()
        newnotice = newnotice + 1
    }

    fun new_notice_out(view:View) {
        new_notice.setVisibility(View.GONE)
        newnotice = newnotice - 1
    }


    // 저장버튼 누르면 1. 이름이 start에서 바뀌고  3. start로 돌아감

   fun save1(view:View) {

       if(newnotice == 0) {
         //1번
        var name_1 : String = editText.text.toString()
        var intent = Intent()
        intent.putExtra("name",name_1)

        setResult(name1, intent)


       //3번
        finish()
       }
   }

}
