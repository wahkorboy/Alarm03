package com.wahkor.alarm03.model

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.wahkor.alarm03.AlarmActivity
import com.wahkor.alarm03.R

const val CHANNEL_ID="com.wahkor.alarm03"
class NotificationModel(var context:Context){
    //Intent = Application
    //PendingIntent = OS
    private lateinit var pendingIntent: PendingIntent
    fun createBuilder(title: String, content: String,intent:Intent): Notification.Builder {
        pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                setContentText(title)
                setContentText(content)
                setPriority(Notification.PRIORITY_MAX)
                setContentIntent(pendingIntent)
            }
        } else {
            Notification.Builder(context).apply {
                setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
                setContentText(title)
                setContentText(content)
                setPriority(Notification.PRIORITY_MAX)
                setContentIntent(pendingIntent)
            }
        }
    }
}