package entities

import kotlinx.serialization.Serializable

@Serializable
data class ToDoList (
    var title: String,
    var entities: MutableList<Task>
)