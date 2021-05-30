package com.wahkor.alarm03.model

class MusicItem {
    var title:String?=null
    var uri:String?=null
    var length:String?=null
    constructor()
    constructor(title:String,uri:String,length:String){
        this.length=length
        this.title=title
        this.uri=uri
    }
}