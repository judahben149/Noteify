package com.judahben149.note.todo.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.judahben149.note.NoteDatabase
import com.judahben149.note.todo.data.TodoDao
import com.judahben149.note.todo.model.Todo

class TodoRepository(private val todoDao: TodoDao) {

    companion object{

        @Volatile
        private var instance: TodoRepository? = null

        fun getInstance(context: Context): TodoRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = NoteDatabase.getDatabase(context)
                    instance = TodoRepository(database.todoDao())
                }
                return instance as TodoRepository
            }
        }
    }

    suspend fun createTodo(todo: Todo) {
        todoDao.createTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    fun readAllTodos(): LiveData<List<Todo>> {
        return todoDao.readAllTodos()
    }



}