import entities.Task
import entities.ToDoList
import utils.Responce

interface Contracts {
    interface FilesInteractor {
        fun findFiles() : MutableList<String>

        fun createNewFile(fileName: String): Responce<Unit>

        fun deleteFile(fileName: String): Responce<Unit>

        fun readFile(fileName: String): Responce<ToDoList>

        fun writeListToFile(fileName: String, toDoList: ToDoList): Responce<Unit>
    }

    interface ListInteractor {
        fun selectCurrentList(newListName: String): Boolean

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