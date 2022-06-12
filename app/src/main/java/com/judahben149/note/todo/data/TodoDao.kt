package com.judahben149.note.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.judahben149.note.todo.model.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun readAllTodos(): LiveData<List<Todo>>
}