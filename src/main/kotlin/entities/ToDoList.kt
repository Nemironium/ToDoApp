package entities

import kotlinx.serialization.Serializable

@Serializable
data class ToDoList (
    val title: String,
    val tasks: MutableList<Task>
)