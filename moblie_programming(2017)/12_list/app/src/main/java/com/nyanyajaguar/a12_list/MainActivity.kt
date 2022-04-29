package com.nyanyajaguar.a12_list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    var items = mutableListOf<MyData>()
    lateinit var adapter: MyAdapter
    var idx_sel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MyAdapter(this, items)

        listView.setAdapter(adapter)
        listView.setOnItemClickListener(this)
    }


    override fun onItemClick(
            parent:AdapterView<*>,view:View,position:Int,id:Long) {
        textView.text = "Click : " + position + ", name : "+items[position].name2
        idx_sel = position

    }

    fun add_item(view: View) {
        items.add(MyData(R.drawable.cup_coffee, "- " + Random().nextInt(100),"입니다") )
        adapter.notifyDataSetChanged()
    }

    fun del_item(view:View){
        items.removeAt(idx_sel)
        adapter.notifyDataSetChanged()
    }


}
