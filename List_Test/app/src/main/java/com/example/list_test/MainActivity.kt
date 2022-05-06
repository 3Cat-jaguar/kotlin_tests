package com.example.list_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*




class MainActivity :
    AppCompatActivity(),
    AdapterView.OnItemClickListener {

    var items = mutableListOf<MyData>() // MyData 타입 클래스가 리스트로 저장
    lateinit var adapter: MyAdapter // 새로 만든 MyAdapter 를 adapter 라는 이름으로 사용할것임
    var idx_sel = 0 // 기본값으로 0번째를 가리키고있

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MyAdapter(this, items)

        listView.setAdapter(adapter) // listView 에서 데이터 읽을 Adapter 를 MyAdapter 로 셋팅한다
        listView.setOnItemClickListener(this) // listView 의 각 데이터를 클릭가능하도록 셋팅한다
    }


    // listView 의 특정 아이템이 클릭될때의 처리
    override fun onItemClick(
        parent:AdapterView<*>, view: View, position:Int, id:Long) {
        textView.text = "Click : " + position + ", name : "+items[position].name2
        idx_sel = position

    }
    // ADD 버튼을 눌렀을 떄의 처리
    fun add_item(view: View) {
        //listView 에 표현될 데이터를 items 로 정의한 리스트에 추가한다. 이를 listView 가 읽게된다
        items.add(MyData(R.drawable.cup_coffee, " : " + Random().nextInt(100),"입니다") )
        // adpter 에게 데이터가 변경되었음을 알리고 변경된 값으로 새로고침하여 보여지도록 한다
        adapter.notifyDataSetChanged()
    }

    fun del_item(view:View){
        // items 라는 리스트에서 idx_sel 번째 데이터가 삭제되도록 한다. removeAt 에서 자동 처리한다. 이 함수는 mutableListOf의 보유 함수이다.
        items.removeAt(idx_sel)
        adapter.notifyDataSetChanged()
    }


}
