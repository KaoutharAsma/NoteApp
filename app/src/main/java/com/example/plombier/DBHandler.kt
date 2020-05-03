package com.example.plombier

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [(Note::class)],version = 1)
abstract class DBNote: RoomDatabase(){

    abstract fun NoteDao():NoteDao
}