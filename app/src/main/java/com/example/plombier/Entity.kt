package com.example.plombier


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note{

    @PrimaryKey(autoGenerate = true)
    var num:Int = 0
    var title=""
    var color:String =""
    var content:String = ""
}