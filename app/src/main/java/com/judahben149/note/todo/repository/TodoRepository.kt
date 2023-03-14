package com.judahben149.note.todo.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.judahben149.note.NoteDatabase
import com.judahben149.note.todo.data.TodoDao
import com.judahben149.note.todo.model.Todo
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {


    @WorkerThread
    fun createTodo(todo: Todo) {
        todoDao.createTodo(todo)
    }

    @WorkerThread
    fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    fun readAllTodos(): LiveData<List<Todo>> {
        return todoDao.readAllTodos()
    }



}