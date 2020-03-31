import entities.Task
import entities.ToDoList

interface Contracts {
    interface FilesInteractor {
        fun findFiles() : MutableList<String>

        fun createNewFile(fileName: String)

        fun deleteFile(fileName: String)

        fun readFile(fileName: String): ToDoList?

        fun writeListToFile(fileName: String, toDoList: ToDoList)
    }

    interface ListInteractor {
        fun selectCurrentList(newListName: String)

        fun createNewList(newListName: String)

        fun deleteList(listName: String)

        fun showTasks(): MutableList<Task>?

        fun showNotDoneTasks(): List<Task>?
    }

    interface TaskInteractor {
        fun addTask(task: Task)

        fun setTaskAsDone(taskId: Int): Boolean

        fun deleteTask(taskId: Int): Boolean
    }
}