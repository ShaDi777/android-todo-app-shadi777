package com.shadi777.todoapp.recyclerview.data

import java.io.Serializable

enum class Priority {
    Default, Low, High
}

data class TodoItem (
    var id: String,
    var text: String,
    var priority: Priority,
    var is_done: Boolean,
    var create_date: Long,
    var deadline_date: Long = 0, // optional
    var change_date: Long = 0 // optional
) : Serializable