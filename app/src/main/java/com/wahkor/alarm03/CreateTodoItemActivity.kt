package com.wahkor.alarm03

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.*
import androidx.core.app.NotificationCompat
import com.wahkor.alarm03.database.DBHelper
import com.wahkor.alarm03.databinding.ActivityCreateTodoItemBinding

import com.wahkor.alarm03.model.TodoItem
import com.wahkor.alarm03.receiver.AlarmReceiver
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateTodoItemActivity : AppCompatActivity() {
    private lateinit var db: DBHelper
    private lateinit var binding: ActivityCreateTodoItemBinding
    private lateinit var todoItem: TodoItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTodoItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DBHelper(this)
        todoItem = EditTodoItem
        if (todoItem.id!! >0) {
            setInitial()
            }
    }

    fun getTime(view: View) {
        todoItem.timeStamp = null
        binding.createAlarmSwitch.isChecked = false
        binding.createAlarmSwitch.visibility = View.GONE
        binding.createTimeTextView.visibility = View.GONE
        binding.createClockImageView.visibility = View.VISIBLE
        Calendar.getInstance().apply {
            DatePickerDialog(
                this@CreateTodoItemActivity,
                0,
                { _, year, month, dayOfMonth ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    TimePickerDialog(
                        this@CreateTodoItemActivity,
                        0,
                        { _, hourOfDay, minute ->
                            this.set(Calendar.HOUR, hourOfDay)
                            this.set(Calendar.MINUTE, minute)
                            setTime(timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setTime(timeInMillis: Long) {
        binding.createClockImageView.visibility = View.GONE
        binding.createAlarmSwitch.visibility = View.VISIBLE
        binding.createAlarmSwitch.text = getString(R.string.alarm_enable)
        binding.createAlarmSwitch.isChecked = true
        binding.createTimeTextView.visibility = View.VISIBLE
        binding.createTimeTextView.text = getStringTime(timeInMillis)
        todoItem.timeStamp = timeInMillis
    }

    fun setSwitch(view: View) {
        if (binding.createAlarmSwitch.isChecked) binding.createAlarmSwitch.text =
            getString(R.string.alarm_enable)
        else binding.createAlarmSwitch.text = getString(R.string.alarm_disable)
    }

    fun addButtonClick(view: View) {
        val title = binding.createTitleEditView.text.toString()
        if (title.isNotBlank()) {
            todoItem.title = title
            todoItem.description = binding.createDescriptionEditView.text.toString()
            todoItem.alarmEnable = binding.createAlarmSwitch.isChecked
            val result=if (todoItem.id!! < 1){
                todoItem.id = System.currentTimeMillis()
                db.addTodo(todoItem)

            }else{
                db.updateTodo(todoItem)
            }
            if (result > 0) {
                setAlarm()
                val intent = Intent(this, DisplayTodoListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "cannot save to DB!!", Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun setInitial() {
        binding.createTitleEditView.setText(todoItem.title)
        binding.createDescriptionEditView.setText(todoItem.description)
        binding.createTimeTextView.text = getStringTime(todoItem.timeStamp!!)
        binding.createAlarmSwitch.isChecked = todoItem.alarmEnable!!
        binding.createAlarmSwitch.text =
            if (todoItem.alarmEnable!!) getString(R.string.alarm_enable) else getString(R.string.alarm_disable)

        binding.createAddButton.text=getString(R.string.create_update_button)
        binding.createDeleteButton.visibility=View.VISIBLE
        if(todoItem.timeStamp!!>0){
            binding.createAlarmSwitch.visibility = View.VISIBLE
            binding.createClockImageView.visibility = View.GONE
            binding.createTimeTextView.visibility = View.VISIBLE
        }


    }

    private fun getStringTime(timeInMills: Long): String {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date = Date(timeInMills)
            dateFormat.format(date)

        } catch (e: Exception) {
            e.toString()
        }
    }

    fun cancelButtonClick(view: View) {
        onBackPressed()
    }
    fun deleteButtonClick(view: View) {
        db.deleteTodo(todoItem)
        val intent=Intent(this,DisplayTodoListActivity::class.java)
        startActivity(intent)
    }
    fun popup(context: Context, text:String){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show()

    }
    private fun setAlarm(){
        val alarmMGR:AlarmManager= getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this,AlarmReceiver::class.java)
        intent.putExtra("notificationID",todoItem.id.toString())
        intent.putExtra("notificationContent",todoItem.title)
        val pendingIntent=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmMGR.setExact(AlarmManager.RTC_WAKEUP,todoItem.timeStamp!!,pendingIntent)
    }
}
