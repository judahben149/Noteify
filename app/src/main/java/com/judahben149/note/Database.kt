package com.judahben149.note

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.judahben149.note.note.data.NoteDao
import com.judahben149.note.note.model.Note
import com.judahben149.note.todo.data.TodoDao
import com.judahben149.note.todo.model.Todo

@Database(entities = [Note::class, Todo::class], exportSchema = false, version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun todoDao(): TodoDao
}
