package com.shadi777.todoapp.recyclerview.data

import java.io.Serializable
import java.util.Date

enum class Priority {
    Default, Low, High
}

data class TodoItem (
    var id: String,
    var text: String,
    var priority: Priority,
    var is_done: Boolean,
    var create_date: Date,
    var deadline_date: Date? = null,// optional
    var change_date: Date? = null // optional
) : Serializable