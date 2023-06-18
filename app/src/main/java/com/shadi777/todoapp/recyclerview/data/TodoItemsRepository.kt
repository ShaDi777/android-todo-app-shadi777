package com.shadi777.todoapp.recyclerview.data

import java.util.Date


class TodoItemsRepository {
    companion object {
        var idIterator: Long = 0
        val itemlist: MutableList<TodoItem> = mutableListOf<TodoItem>()
    }

    init {
        for(i in 1..6) {
            val id: String = idIterator.toString()
            val text = "Пример дела #${i}"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 7..12) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nЕсть 2 строка"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 13..18) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nЕсть 2 строка\nЕсть 3 строка"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 19..24) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nВ этом деле много слов, поэтому после переполнения должно стоять многоточие"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()

            val todoItem = TodoItem(id, text, priority, is_done, create_date)
            addTodoItem(todoItem)
        }
        for(i in 25..30) {
            val id: String = i.toString()
            val text: String = "Пример дела #${i}\nС дедлайном на завтра"
            val priority:Priority = Priority.values()[i % 3]
            val is_done = i % 2 != 0
            val create_date: Date = Date()
            val deadline_date: Date = Date()
            deadline_date.time += 86400 * 1000

            val todoItem = TodoItem(id, text, priority, is_done, create_date, deadline_date)
            addTodoItem(todoItem)
        }
    }

    fun addTodoItem(item: TodoItem) {
        idIterator++
        itemlist.add(item)
    }

    fun removeTodoItem(item: TodoItem) = itemlist.removeIf { item.id == it.id }

    fun countDone(): Int = itemlist.count { it.is_done }

    fun getTasks(isDoneVisible: Boolean): List<TodoItem> {
        if (isDoneVisible) {
            return itemlist
        } else {
            return buildList {
                for (item in itemlist)
                    if (!item.is_done)
                        add(item)
            }
        }
    }

}