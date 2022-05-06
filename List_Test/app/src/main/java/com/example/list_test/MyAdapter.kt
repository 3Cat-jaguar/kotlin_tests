package com.example.list_test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

data class MyData(var img : Int, var name1 : String, var name2 : String)

class MyAdapter(context: Context, list:MutableList<MyData>) :
    ArrayAdapter<MyData>(context,0,list){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var data = getItem(position)

        var a = LayoutInflater.from(context)

        var view = convertView
        if(view == null) {
            view = a.inflate(R.layout.list_row,parent,false)
        }

        var list_text1 = view!!.findViewById<TextView>(R.id.mytext1)
        var list_text2 = view!!.findViewById<TextView>(R.id.mytext2)
        var list_image = view.findViewById<ImageView>(R.id.myimage)

        if (data != null) {
            list_text1.text = data.name1
            list_text2.text = data.name2
            list_image.setImageResource(data.img)
        }

        return view
    }


}