package com.wahkor.alarm03.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.wahkor.alarm03.R
import com.wahkor.alarm03.model.TodoItem
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter(var context:Context,var arraylst:ArrayList<TodoItem>):BaseAdapter() {
    override fun getCount(): Int {
        return  arraylst.size
    }

    override fun getItem(position: Int): Any {
        return arraylst[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val view=View.inflate(context, R.layout.todo_adapter_layout,null)
        val description=view.findViewById<TextView>(R.id.adapterTodoDescription)
        val title=view.findViewById<TextView>(R.id.adapterTodoTitle)
        val time=view.findViewById<TextView>(R.id.adapterTodotime)
        val todoItem=arraylst[position]
        description.text=todoItem.description
        time.text=getStringTime(todoItem.timeStamp!!)
        title.text=todoItem.title

        return view
    }
    private fun getStringTime(timeInMills:Long):String{
        return try {
            val dateFormat=SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date= Date(timeInMills)
            if(timeInMills<1) "" else dateFormat.format(date)

        }catch (e:Exception){
            e.toString()
        }
    }
}