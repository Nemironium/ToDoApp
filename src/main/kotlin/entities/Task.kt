package entities

import kotlinx.serialization.Serializable

@Serializable
data class Task (
    var id: Int = 99,
    var name: String,
    var description: String = "",
    var status: Int = 0
)