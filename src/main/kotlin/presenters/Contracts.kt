package presenters

import entities.TaskStatus
import entities.Task

interface Contracts {

    interface TaskPresenterContract {
        fun createNewList(name: String)

        fun deleteList()

        fun selectNewList(listPath: String)

        fun createNewTask(task: Task)

        fun deleteTask(task: Task)

        fun changeTaskStatus(task: Task, newTaskStatus: TaskStatus)

        fun showAllTasks()

        fun showCompletedTasks()
    }

    interface View {

    }

}