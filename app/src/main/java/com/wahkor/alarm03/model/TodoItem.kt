package com.wahkor.alarm03.model


class TodoItem {
    var id:Long?=0
    var timeStamp:Long?=null
    var title:String?=null
    var description:String?=null
    var alarmEnable:Boolean?=null

    constructor()
    constructor(id:Long,timeStamp: Long,title:String,description:String,alarmEnable:Boolean){
        this.id=id
        this.timeStamp=timeStamp
        this.title=title
        this.description=description
        this.alarmEnable=alarmEnable
    }
}