package com.wahkor.alarm03.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.wahkor.alarm03.AlarmActivity
import com.wahkor.alarm03.NotificationManagerCalling
import com.wahkor.alarm03.model.NotificationModel


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent!!.getStringExtra("notificationID")
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        alarmIntent.putExtra("alarmID", id)
        val content = intent.getStringExtra("notificationContent")
        val builder = NotificationModel(context!!).createBuilder(id!!, content!!, alarmIntent)
        val notificationManagerCalling = NotificationManagerCalling(context, builder)
        notificationManagerCalling.createChannel("my title")
        notificationManagerCalling.startNotify()
        //context.startActivity(alarmIntent)
    }

}