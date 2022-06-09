package com.judahben149.note.todo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.judahben149.note.todo.model.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)
}