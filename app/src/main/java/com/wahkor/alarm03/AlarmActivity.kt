package com.wahkor.alarm03

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.wahkor.alarm03.database.DBHelper

class AlarmActivity : AppCompatActivity() {
    private lateinit var db:DBHelper
    private lateinit var mediaPlayer:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        db= DBHelper(this)
        val text=intent.getStringExtra("alarmID")
        val todoItem=db.singleTodoItem(text!!.toLong())

        val textview=findViewById<TextView>(R.id.alarmTextView)
        textview.text=todoItem.title
        mediaPlayer=MediaPlayer.create(this,R.raw.touch_my_body_sistar)
        mediaPlayer.start()
        mediaPlayer.seekTo(10*1000)
    }

    override fun onBackPressed() {
        val intent = Intent(this,DisplayTodoListActivity::class.java)
        startActivity(intent)
    }

    fun stopAlarm(view: View) {
        mediaPlayer.stop()
    }
}