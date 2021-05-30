package com.wahkor.alarm03

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.wahkor.alarm03.model.TodoItem

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EditTodoItem= TodoItem()
    }

    fun addTodoButtonClick(view: View) {
        val intent= Intent(this,CreateTodoItemActivity::class.java)
        startActivity(intent)}
    fun displayTodoListButtonClick(view: View) {
        val intent= Intent(this,DisplayTodoListActivity::class.java)
        startActivity(intent)}

    override fun onBackPressed() {

    }

    fun playMusic(view: View) {
        val intent=Intent(this,MusicActivity::class.java)
        startActivity(intent)
    }

}