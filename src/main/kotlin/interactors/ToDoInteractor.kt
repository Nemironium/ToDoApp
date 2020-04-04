package interactors

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

    val isTasksAvailable: Boolean?
        get() = currentList?.tasks?.isNotEmpty()

    val maxTaskId: Int?
        get() = currentList?.tasks?.maxBy { it.id }?.id

    override fun selectCurrentList(newListName: String): Result<ToDoList> {
        val result = filesInteractor.readFile(newListName.toListFileName())

        if (result.isSuccessful)
            currentList = result.data

        return result
    }

    override fun createNewList(newListName: String): Result<Unit> {
        val newList = ToDoList(newListName, mutableListOf())
        val result = filesInteractor.createNewFile(newListName.toListFileName(), newList)

        if (result.isSuccessful)
            currentList = newList

        return result
    }

    override fun deleteList(listName: String): Result<Unit> {
        val result = filesInteractor.deleteFile(listName.toListFileName())

        if (result.isSuccessful && listName == currentList?.title)
            currentList = null

        return result
    }

    override fun getTasks(): List<Task>? = currentList?.tasks

    override fun getTodoTasks(): List<Task>? = currentList?.todoTasks()

    override fun addTask(task: Task): Result<Unit> {
        val tempList = currentList?.copy()
        tempList?.addTask(task)

        val result = updateListFile(tempList)
        if (result.isSuccessful)
            currentList = tempList

        return result
    }

    override fun setTaskAsDone(taskId: Int): Result<Unit>
            = modifyTask(taskId, TaskAction.SET_AS_DONE)

    override fun deleteTask(taskId: Int): Result<Unit>
            = modifyTask(taskId, TaskAction.DELETE)

    override fun searchTaskByTitle(keyword: String): List<Task> =
        currentList?.tasks?.filter { it.name.contains(keyword) }.orEmpty()

    private fun updateListFile(toDoList: ToDoList?): Result<Unit> =
        if (toDoList != null)
            filesInteractor.writeListToFile(toDoList.title.toListFileName(), toDoList)
        else
            Result.error("ToDo list with name ${toDoList?.title} does not exist")

    private fun modifyTask(taskId: Int, taskAction: TaskAction): Result<Unit> {
        val tempList = currentList?.copy()

        val result: Result<Unit>
        val action: Boolean

        action = when(taskAction) {
            TaskAction.DELETE -> tempList?.removeTask(taskId) == true
            TaskAction.SET_AS_DONE -> tempList?.setTaskAsDone(taskId) == true
        }

        if (action) {
            result = updateListFile(tempList)
            if (result.isSuccessful)
                currentList = tempList
        } else {
            result = Result.error("Task with id $taskId does not exist or already marked as completed")
        }

        return result
    }
}
