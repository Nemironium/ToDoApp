package presenters

import entities.TaskStatus
import entities.Task

class TaskPresenter(private val listPath: String) : Contracts.TaskPresenterContract {
    override fun createNewList(name: String) {
        TODO("Not yet implemented")
    }

    override fun deleteList() {
        TODO("Not yet implemented")
    }

    override fun selectNewList(listPath: String) {
        TODO("Not yet implemented")
    }

    override fun createNewTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun changeTaskStatus(task: Task, newTaskStatus: TaskStatus) {
        TODO("Not yet implemented")
    }

    override fun showAllTasks() {
        TODO("Not yet implemented")
    }

    override fun showCompletedTasks() {
        TODO("Not yet implemented")
    }

}