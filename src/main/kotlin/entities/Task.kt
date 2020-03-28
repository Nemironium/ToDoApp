package entities

import kotlinx.serialization.Serializable

@Serializable
data class Task (
    var id: Int,
    var name: String,
    var description: String,
    var status: Status = Status.TODO
)