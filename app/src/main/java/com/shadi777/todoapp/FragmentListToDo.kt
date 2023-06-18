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
import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository.Companion.idIterator
import com.shadi777.todoapp.recyclerview.data.TodoItemsRepository.Companion.itemlist
import java.util.Date


class FragmentListToDo : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val todoAdapter = TodoAdapter()
    private val todoItemsRepository = TodoItemsRepository()
    private var isDoneVisible = true

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
                    idIterator.toString(),
                    "",
                    Priority.Default,
                    false,
                    Date()
                )
            val action =
                FragmentListToDoDirections.actionFragmentListToDoToFragmentCreateToDo(todoItem)
            Navigation.findNavController(view).navigate(action)
        }

        initRecyclerView()

        binding.textViewDone.text = String.format(
            getResources().getString(R.string.done_sublabel),
            todoItemsRepository.countDone()
        )

        setVisibleTasksAndIcon(isDoneVisible)

        binding.imageViewVisible.setOnClickListener {
            isDoneVisible = !isDoneVisible
            setVisibleTasksAndIcon(isDoneVisible)
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)

        todoAdapter.setOnChangeItemListener {
            binding.textViewDone.text = String.format(
                getResources().getString(R.string.done_sublabel),
                todoItemsRepository.countDone()
            )

            todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
        }
    }

    private fun setVisibleTasksAndIcon(isDoneVisible: Boolean) {
        when (isDoneVisible) {
            true -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility)
            else -> binding.imageViewVisible.setImageResource(R.drawable.ic_visibility_off)
        }
        todoAdapter.submitList(todoItemsRepository.getTasks(isDoneVisible))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}