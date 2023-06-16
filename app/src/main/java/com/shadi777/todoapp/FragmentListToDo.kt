package com.shadi777.todoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.shadi777.todoapp.databinding.FragmentListToDoBinding
import com.shadi777.todoapp.recyclerview.TodoAdapter
import com.shadi777.todoapp.recyclerview.data.Action
import com.shadi777.todoapp.recyclerview.data.Priority
import com.shadi777.todoapp.recyclerview.data.SharedTodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository
import java.util.Date


class FragmentListToDo : Fragment() {

    private val todoItemsRepository = TodoItemsRepository()
    private var isDoneVisible = false

    private var _binding: FragmentListToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SharedTodoItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListToDoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SharedTodoItem::class.java)
        viewModel.action.observe(viewLifecycleOwner, Observer<Action> { receivedAction ->
            when (receivedAction) {
                Action.SAVE_NEW -> {
                    todoItemsRepository.addTodoItem(viewModel.todoItem.value!!)
                }

                Action.DELETE -> {
                    todoItemsRepository.removeTodoItem(viewModel.todoItem.value!!)
                }

                else -> {}
            }
        })

        binding.buttonAddTask.setOnClickListener {
            val todoItem =
                TodoItem(
                    todoItemsRepository.idIterator.toString(),
                    "",
                    Priority.Default,
                    false,
                    Date()
                )
            val action =
                FragmentListToDoDirections.actionFragmentListToDoToFragmentCreateToDo(todoItem)
            Navigation.findNavController(view).navigate(action)
        }


        val recyclerView: RecyclerView = binding.recyclerView
        val todoAdapter = TodoAdapter()
        val layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = layoutManager
        todoAdapter.itemlist = todoItemsRepository.itemlist

        binding.textViewDone.text = String.format(
            getResources().getString(R.string.done_sublabel),
            todoItemsRepository.countDone()
        )

        binding.imageViewVisible.setOnClickListener {
            when (isDoneVisible) {
                true -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility_off)
                else -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility)
            }
            isDoneVisible = !isDoneVisible
            todoAdapter.itemlist = todoItemsRepository.getTasks(isDoneVisible)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}