package com.wahkor.alarm03.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.wahkor.alarm03.model.TodoItem

class DBHelper(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "todo1.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME="TodoTable"
        const val COL_ID="id"
        const val COL_TIMESTAMP="timeStamp"
        const val COL_TITLE="title"
        const val COL_DESCRIPTION="description"
        const val COL_ALARMENABLE="alarmEnable"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val queryString = "create table $TABLE_NAME( $COL_ID INTEGER PRIMARY KEY ," +
                " $COL_TIMESTAMP INTEGER , $COL_TITLE TEXT , $COL_DESCRIPTION text , $COL_ALARMENABLE INTEGER)"
        db!!.execSQL(queryString)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //CRUDE
    val allToDOItem:ArrayList<TodoItem>
        get() {
            val list= ArrayList<TodoItem>()
            val queryString="SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(queryString,null)
            if (cursor.moveToFirst()){
                do {
                    val todoItem = TodoItem()
                    todoItem.id=cursor.getLong(cursor.getColumnIndex(COL_ID))
                    todoItem.timeStamp=cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP))
                    todoItem.title=cursor.getString(cursor.getColumnIndex(COL_TITLE))
                    todoItem.description=cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION))
                    todoItem.alarmEnable=(cursor.getInt(cursor.getColumnIndex(COL_ALARMENABLE))==1)
                    list.add(todoItem)
                }while (cursor.moveToNext())
            }

            return list
        }

    fun singleTodoItem(id:Long):TodoItem {
        val todoItem=TodoItem()
        val queryString = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"
        val db= this.writableDatabase
        val cursor = db.rawQuery(queryString,null)
        if (cursor.moveToFirst()){
            todoItem.id=cursor.getLong(cursor.getColumnIndex(COL_ID))
            todoItem.timeStamp=cursor.getLong(cursor.getColumnIndex(COL_TIMESTAMP))
            todoItem.title=cursor.getString(cursor.getColumnIndex(COL_TITLE))
            todoItem.description=cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION))
            todoItem.alarmEnable= cursor.getInt(cursor.getColumnIndex(COL_ALARMENABLE))==1
        }
        db.close()
        return todoItem
    }

    fun addTodo(todoItem:TodoItem):Long{
        val db=this.writableDatabase
        val values=ContentValues()
        values.put(COL_ID,todoItem.id)
        values.put(COL_TIMESTAMP,todoItem.timeStamp)
        values.put(COL_TITLE,todoItem.title)
        values.put(COL_DESCRIPTION,todoItem.description)
        values.put(COL_ALARMENABLE,todoItem.alarmEnable)
        val add = db.insert(TABLE_NAME,null,values)
        db.close()
        return add
    }

    fun updateTodo(todoItem:TodoItem):Long{
        val db=this.writableDatabase
        val values=ContentValues()
        val enableInt = if(todoItem.alarmEnable!!) 1 else 0
        values.put(COL_ID,todoItem.id)
        values.put(COL_TIMESTAMP,todoItem.timeStamp)
        values.put(COL_TITLE,todoItem.title)
        values.put(COL_DESCRIPTION,todoItem.description)
        values.put(COL_ALARMENABLE,enableInt)
        val update = db.update(TABLE_NAME,values,"$COL_ID=?", arrayOf(todoItem.id.toString()))
        db.close()
        return update.toLong()
    }

    fun deleteTodo(todoItem:TodoItem):Int{
        val db=this.writableDatabase
        val delete = db.delete(TABLE_NAME,"$COL_ID=?", arrayOf(todoItem.id.toString()))
        db.close()
        return delete
    }
}