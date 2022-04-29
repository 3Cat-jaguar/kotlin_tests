package com.nyanyajaguar.term_project_162phg02_dicepoker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new2.*


var img_data2 : Uri? = null
var new2notice = 0

class NEW2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new2)
    }

    fun select_photo(view: View) {
        if(new2notice == 0) {
            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, SELECT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode:Int, data:Intent) {

        if(requestCode == SELECT && resultCode == RESULT_OK) {
            Picasso.with(this).load(data.data).into(imageView2)

            img_data2 = data.data
        }
    }

    fun new2_notice_in(view:View) {
        new2_notice.setVisibility(View.VISIBLE)
        editText2.setClickable(false)
        new2_notice.bringToFront()
        new2_notice.invalidate()
        new2notice = new2notice + 1
    }

    fun new2_notice_out(view:View) {
        new2_notice.setVisibility(View.GONE)
        new2notice = new2notice - 1
    }

    // 저장버튼 누르면 1. p2dml이름이 start에서 바뀌고  3. start로 돌아감

    fun save2(view:View) {
        if(new2notice == 0) {
            //1번
            var name_2 : String = editText2.text.toString()
            var intent = Intent()
            intent.putExtra("name",name_2)
            setResult(name2, intent)


            //3번
            finish()
        }
    }

}
