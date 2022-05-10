package com.example.for02_aboutme

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.for02_aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

//    lateinit var editText : EditText
//    lateinit var doneButton : Button
//    lateinit var nicknameTextView : TextView

    private val myName:MyName = MyName("ylee")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 을 이용하면 findViewById 를 이용하지 않아도 View 의 데이터를 가져올 수 있다.
        // binding 은 View 의 id 를 camel 형으로 변환하여 자동으로 데이터화한다.
        // ex) id 가 nickname_edit 인 View 의 데이터는 nicknameEdit 으로 만들어준다.
        // 때문에 MainActivity 에서 findViewById 를 이용하여 변수를 새로 만들 필요가 없다.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myName = myName
//        setContentView(R.layout.activity_main)
//        editText = findViewById<EditText>(R.id.nickname_edit)
//        doneButton = findViewById<Button>(R.id.done_button)
//        nicknameTextView = findViewById<TextView>(R.id.nickname_text)

//        doneButton.setOnClickListener { addNickname(it) }
        binding.doneButton.setOnClickListener { addNickname(it) }
        //여기에서 it 는 클릭 대상인 done_button 을 가리킨다

//        nicknameTextView.setOnClickListener { updateNickname(it) }
        binding.nicknameText.setOnClickListener { updateNickname(it) }
    }

    // 여기에서 view 는 클릭된 대상인 done_button 을 가리킨다
    private fun addNickname(view: View){
//        nicknameTextView.text = editText.text
//
//        editText.visibility = View.GONE
//        view.visibility = View.GONE
//        nicknameTextView.visibility = View.VISIBLE

//        binding.nicknameText.text = binding.nicknameEdit.text
        binding.apply {
            myName?.nickname = binding.nicknameEdit.text.toString()
            invalidateAll()
        }

        binding.nicknameEdit.visibility = View.GONE
        view.visibility = View.GONE
        binding.nicknameText.visibility = View.VISIBLE

        //done 버튼 눌렸을 때 edit 에 사용된 keyboard 내리기
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateNickname(view:View) {
//        editText.visibility = View.VISIBLE
//        doneButton.visibility = View.VISIBLE
//        view.visibility = View.GONE

        binding.nicknameEdit.visibility = View.VISIBLE
        binding.doneButton.visibility = View.VISIBLE
        view.visibility = View.GONE

        // textView 다시 눌러 edit 가능하도록 할 때 키보드도 같이 올라오도록 처리
//        editText.requestFocus()
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(editText, 0)

        binding.nicknameEdit.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.nicknameEdit, 0)
    }
}