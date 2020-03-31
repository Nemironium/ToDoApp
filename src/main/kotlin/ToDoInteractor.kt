import entities.Task
import entities.ToDoList
import kotlinx.serialization.UnstableDefault
import utils.*

@UnstableDefault
class ToDoInteractor(private var listName: String, private val filesInteractor: FilesInteractor)
    : Contracts.ListInteractor, Contracts.TaskInteractor {

    private var currentList: ToDoList? = null
    val availableLists: MutableList<String>
        get() = filesInteractor.findFiles()

    init {
        currentList = filesInteractor.readFile(listName.toListFileName())
    }

    override fun selectCurrentList(newListName: String) {
        listName = newListName
        currentList = filesInteractor.readFile(newListName.toListFileName())
    }

    override fun createNewList(newListName: String) {
        filesInteractor.createNewFile(newListName.toListFileName())
        selectCurrentList(newListName)
    }

    override fun deleteList(listName: String) {
        filesInteractor.deleteFile(listName.toListFileName())
        currentList = null
    }

    override fun showTasks(): MutableList<Task>? = currentList?.tasks

    override fun showNotDoneTasks(): List<Task>? = currentList?.todoTasks()

    override fun addTask(task: Task) {
        currentList?.addTask(task)
        updateListFile()
    }

    override fun setTaskAsDone(taskId: Int): Boolean {
        val result = currentList?.setTaskAsDone(taskId)
        updateListFile()
        return result!!
    }

    override fun deleteTask(taskId: Int): Boolean {
        val result = currentList?.removeTask(taskId)
        updateListFile()
        return result!!
    }

    private fun updateListFile() = filesInteractor.writeListToFile(listName.toListFileName(), currentList!!)
}