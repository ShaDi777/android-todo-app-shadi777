package com.shadi777.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.shadi777.todoapp.databinding.ItemTodoBinding
import com.shadi777.todoapp.recyclerview.data.TodoItem
import com.shadi777.todoapp.recyclerview.data.TodoItemDiffCallBack


class TodoAdapter : RecyclerView.Adapter<ViewHolder>() {

    var itemlist = listOf<TodoItem>()
        set(value) {
            val callback = TodoItemDiffCallBack(
                oldItems = field,
                newItems = value,
            )
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoItemViewHolder(binding)
    }

    override fun getItemCount(): Int = itemlist.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TodoItemViewHolder).onBind(itemlist.get(position))
    }

}
