package com.example.plombier

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    fun addNote(note: Note)

    @Query("Select * from Note")
    fun getNote():List<Note>

    @Delete
    fun delete(note: Note)


    @Update
    fun update(note: Note)
}