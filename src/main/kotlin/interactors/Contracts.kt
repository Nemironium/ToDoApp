package interactors

import entities.Task
import entities.ToDoList
import utils.Result

interface Contracts {
    interface FilesInteractor {
        fun findFiles() : MutableList<String>

        fun createNewFile(fileName: String, newToDoList: ToDoList): Result<Unit>

        fun deleteFile(fileName: String): Result<Unit>

        fun readFile(fileName: String): Result<ToDoList>

        fun writeListToFile(fileName: String, toDoList: ToDoList): Result<Unit>
    }

    interface ListInteractor {
        fun selectCurrentList(newListName: String): Result<ToDoList>

        fun createNewList(newListName: String): Result<Unit>

        fun deleteList(listName: String): Result<Unit>
    }

    interface TaskInteractor {
        fun getTasks(): List<Task>?

        fun getTodoTasks(): List<Task>?

        fun addTask(task: Task): Result<Unit>

        fun setTaskAsDone(taskId: Int): Result<Unit>

        fun deleteTask(taskId: Int): Result<Unit>

        fun searchTaskByTitle(keyword: String): List<Task>
    }
}