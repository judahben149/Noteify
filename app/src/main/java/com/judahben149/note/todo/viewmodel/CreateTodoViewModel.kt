package com.judahben149.note.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.judahben149.note.todo.model.Todo
import com.judahben149.note.todo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateTodoViewModel(private val todoRepository: TodoRepository): ViewModel() {

    fun createTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.createTodo(todo)
        }
    }
}