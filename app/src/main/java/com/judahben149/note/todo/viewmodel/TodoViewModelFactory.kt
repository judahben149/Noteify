package com.judahben149.note.todo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.judahben149.note.todo.repository.TodoRepository

class TodoViewModelFactory private constructor(private val todoRepository: TodoRepository) :
    ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: TodoViewModelFactory? = null

        fun getInstance(context: Context): TodoViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: TodoViewModelFactory(TodoRepository.getInstance(context))
            }
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(CreateTodoViewModel::class.java) -> {
                CreateTodoViewModel(todoRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class" + modelClass.name)
        }
}