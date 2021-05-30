package com.wahkor.alarm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.wahkor.alarm03.adapter.TodoAdapter
import com.wahkor.alarm03.database.DBHelper
import com.wahkor.alarm03.databinding.ActivityDisplayTodoListBinding
import com.wahkor.alarm03.databinding.TodoAdapterLayoutBinding
import com.wahkor.alarm03.model.TodoItem

var EditTodoItem = TodoItem()

class DisplayTodoListActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_todo_list)
        EditTodoItem = TodoItem()
        db = DBHelper(this)
        val listView = findViewById<ListView>(R.id.displayListView)
        val arraylst = db.allToDOItem
        val adapter = TodoAdapter(this, arraylst)
        listView.adapter = adapter
        adapter.notifyDataSetInvalidated()
        listView.setOnItemClickListener { parent, view, position, id ->
            EditTodoItem = arraylst[position]
            val intent = Intent(this, CreateTodoItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun addNewTask(view: View) {
        EditTodoItem=TodoItem()
        val intent = Intent(this,CreateTodoItemActivity::class.java)
        startActivity(intent)
    }
}