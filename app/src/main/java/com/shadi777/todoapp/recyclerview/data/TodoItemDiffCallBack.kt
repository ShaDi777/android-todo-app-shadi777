package com.shadi777.todoapp.recyclerview.data

import androidx.recyclerview.widget.DiffUtil

class TodoItemDiffCallBack(
    val oldItems: List<TodoItem>,
    val newItems: List<TodoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.get(oldItemPosition)
        val newItem = newItems.get(newItemPosition)

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.get(oldItemPosition)
        val newItem = newItems.get(newItemPosition)

        return (oldItem.is_done == newItem.is_done) &&
                (oldItem.priority == newItem.priority) &&
                (oldItem.text == newItem.text) &&
                (oldItem.deadline_date == newItem.deadline_date)
    }
}