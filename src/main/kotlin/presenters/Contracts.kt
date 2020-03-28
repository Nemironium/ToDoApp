package presenters

import entities.Status
import entities.Task

interface Contracts {

    interface TaskPresenterContract {
        fun createNewList(name: String)

        fun deleteList()

        fun selectNewList(name: String)

        fun createNewTask(task: Task)

        fun deleteTask(task: Task)

        fun changeStatus(task: Task, newStatus: Status)


    }

}