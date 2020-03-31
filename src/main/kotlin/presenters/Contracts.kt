package presenters

import entities.Status
import entities.Task

interface Contracts {

    interface TaskPresenterContract {
        fun createNewList(name: String)

        fun deleteList()

        fun selectNewList(listPath: String)

        fun createNewTask(task: Task)

        fun deleteTask(task: Task)

        fun changeTaskStatus(task: Task, newStatus: Status)

        fun showAllTasks()

        fun showCompletedTasks()
    }

    interface View {

    }

}