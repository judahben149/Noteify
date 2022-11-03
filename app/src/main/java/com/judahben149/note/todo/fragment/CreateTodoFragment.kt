package com.judahben149.note.todo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.judahben149.note.R
import com.judahben149.note.databinding.FragmentCreateTodoBinding
import com.judahben149.note.todo.model.Todo
import com.judahben149.note.todo.viewmodel.CreateTodoViewModel
import com.judahben149.note.todo.viewmodel.TodoViewModelFactory


class CreateTodoFragment : Fragment() {

    private var _binding: FragmentCreateTodoBinding? = null
    val binding get() =  _binding!!

    private lateinit var viewModel: CreateTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateTodoBinding.inflate(inflater, container, false)

        val factory = TodoViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[CreateTodoViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSaveTodoCreateTodoScreen.setOnClickListener {
            createTodo()
            Navigation.findNavController(binding.root).navigate(R.id.action_createTodoFragment_to_noteListFragment)
        }

    }

    private fun createTodo() {
        val todoBody = binding.etTodoBodyDetailsScreen.text.toString()
        val dueDate = System.currentTimeMillis()
        val dueTime = System.currentTimeMillis()

        val todo = Todo(0, todoBody, dueDate, dueTime, isTodoPriority = false, isTodoCompleted = false)

        viewModel.createTodo(todo)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}