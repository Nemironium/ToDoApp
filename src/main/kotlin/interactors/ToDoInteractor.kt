package interactors

import Contracts
import entities.Task
import entities.TaskAction
import entities.ToDoList
import kotlinx.serialization.UnstableDefault
import utils.*

@UnstableDefault
class ToDoInteractor(private val filesInteractor: FilesInteractor)
    : Contracts.ListInteractor, Contracts.TaskInteractor {

    private var currentList: ToDoList? = null

    val availableLists: MutableList<String>
        get() = filesInteractor.findFiles()

    val listName: String?
        get() = currentList?.title

    override fun selectCurrentList(newListName: String): Result<ToDoList> {
        val result = filesInteractor.readFile(newListName.toListFileName())

        if (result.status == Status.SUCCESS)
            currentList = result.data

        return result
    }

    override fun createNewList(newListName: String): Result<Unit> {
        val newList = ToDoList(newListName, mutableListOf())
        val result = filesInteractor.createNewFile(newListName.toListFileName(), newList)

        if (result.status == Status.SUCCESS)
            currentList = newList

        return result
    }

    override fun deleteList(listName: String): Result<Unit> {
        val result = filesInteractor.deleteFile(listName.toListFileName())

        if (result.status == Status.SUCCESS && listName == currentList?.title)
            currentList = null

        return result
    }

    override fun showTasks(): MutableList<Task>? = currentList?.tasks

    override fun showNotDoneTasks(): List<Task>? = currentList?.todoTasks()

    override fun addTask(task: Task): Result<Unit> {
        val tempList = currentList?.copy()
        tempList?.addTask(task)

        val result = updateListFile(tempList)
        if (result.status == Status.SUCCESS)
            currentList = tempList

        return result
    }

    override fun setTaskAsDone(taskId: Int): Result<Unit>
            = modifyTask(taskId, TaskAction.SET_AS_DONE)

    override fun deleteTask(taskId: Int): Result<Unit>
            = modifyTask(taskId, TaskAction.DELETE)

    private fun updateListFile(toDoList: ToDoList?): Result<Unit> =
        if (toDoList != null)
            filesInteractor.writeListToFile(toDoList.title.toListFileName(), toDoList)
        else
            Result.error("ToDo list with name ${toDoList?.title} does not exist")

    private fun modifyTask(taskId: Int, taskAction: TaskAction): Result<Unit> {
        val tempList = currentList?.copy()

        val result: Result<Unit>
        val action: Boolean

        when(taskAction) {
            TaskAction.DELETE -> action = tempList?.removeTask(taskId) == true
            TaskAction.SET_AS_DONE -> action = tempList?.setTaskAsDone(taskId) == true
        }

        if (action) {
            result = updateListFile(tempList)
            if (result.status == Status.SUCCESS)
                currentList = tempList
        } else {
            result = Result.error("Task with id $taskId does not exist")
        }

        return result
    }
}
