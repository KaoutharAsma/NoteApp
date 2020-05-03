package com.example.plombier


class NoteManager(build: DBNote) {
    var db:DBNote = build

    fun getNotes():MutableList<Note>{
        db.NoteDao().getNote().toMutableList().forEach{
            println(" inter $it")
        }
        return db.NoteDao().getNote().toMutableList()
    }

    fun deleteNotes(Note: Note): MutableList<Note> {

        db.NoteDao().delete(Note)
        return getNotes()
    }

    fun editNote(Note: Note): MutableList<Note> {
        db.NoteDao().update(Note)
        return getNotes()
    }
    fun addNote(Note: Note): MutableList<Note> {

        db.NoteDao().addNote(Note)
        getNotes().forEach{
            println(" inter $it")
        }

        return getNotes()

    }




}


