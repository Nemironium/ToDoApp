package console

import Application.Companion.closeApp
import entities.Task
import entities.TaskAction
import entities.TaskStatus
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import utils.Result

@UnstableDefault
class TaskConsole(
    private val consoleMenu: ConsoleMenu,
    private val interactor: ToDoInteractor
) : Contracts.TaskConsole {

    override fun taskMenu() {
        when(consoleMenu.getTaskMenuValue(interactor.isTasksAvailable)) {
            null -> closeApp()
            1 -> processTaskCreating()
            2 -> processTaskDeleting()
            3 -> processTaskCompleting()
            4 -> processShowAllTasks()
            5 -> processShowTodoTasks()
        }
    }

    override fun processTaskCreating() {
        val taskName = consoleMenu.getNewName("task name", null)
        val taskDescription = consoleMenu.getNewName("task description (optional)", "")

        if (taskName != null) {
            val newTask = Task(name = taskName, description = taskDescription.orEmpty())

            val result = interactor.addTask(newTask)
            if (result.isFailed)
                consoleMenu.showError(result.message)
            taskMenu()
        } else {
            closeApp()
        }
    }

    override fun processTaskDeleting()
            = processTaskEditing(TaskAction.DELETE)

    override fun processTaskCompleting()
            = processTaskEditing(TaskAction.SET_AS_DONE)

    override fun processShowAllTasks()
            = outTasks(interactor.getTasks().orEmpty())

    override fun processShowTodoTasks()
            = outTasks(interactor.getTodoTasks().orEmpty())

    private fun processTaskEditing(taskAction: TaskAction) {
        when(consoleMenu.getSelectedTaskMenuValue()) {
            null -> closeApp()
            1 -> processFindById(taskAction)
            2 -> processFindByKeyword(taskAction)
        }
    }

    private fun processFindById(taskAction: TaskAction) {
        val taskId = consoleMenu.getTaskId(interactor.maxTaskId)
        val result: Result<Unit>
        if (taskId != null) {
            result = when(taskAction) {
                TaskAction.SET_AS_DONE -> interactor.setTaskAsDone(taskId)
                TaskAction.DELETE -> interactor.deleteTask(taskId)
            }
            if (result.isFailed)
                consoleMenu.showError(result.message)
            taskMenu()
        } else {
            closeApp()
        }
    }

    private fun processFindByKeyword(taskAction: TaskAction) {
        val keyword = consoleMenu.getNewName("keyword", null)

        if (keyword != null) {
            val queryTasks = interactor.searchTaskByTitle(keyword)
            when {
                queryTasks.size > 10 -> {
                    val errText = "There are more than 10 tasks with this keyword." +
                            "Try set another keyword or find by task number"
                    consoleMenu.showError(errText)
                    processTaskEditing(taskAction)
                }
                queryTasks.isEmpty() -> {
                    val errText = "No tasks with this keyword." +
                            "Try set another keyword or find task by number"
                    consoleMenu.showError(errText)
                    processTaskEditing(taskAction)
                }
                else -> {
                    when(taskAction) {
                        TaskAction.DELETE -> {
                            consoleMenu.showTasks(queryTasks)
                        }
                        TaskAction.SET_AS_DONE -> {
                            val todoQueryTasks = queryTasks.filter { it.status == TaskStatus.TODO.code }
                            consoleMenu.showTasks(todoQueryTasks)
                        }
                    }

                    processFindById(taskAction)
                }
            }
        } else {
            closeApp()
        }
    }

    private fun outTasks(tasks: List<Task>) {
        val volume = tasks.size / 50

        if (volume <= 1) {
            consoleMenu.showTasks(tasks)
            taskMenu()
        } else {
            var inputCtr = 0
            print50Tasks(tasks, inputCtr++)

            val confirmationText = "There are more than 50 tasks in List. Show next 50?"
            while (true) {
                if (inputCtr == volume) taskMenu()
                when (consoleMenu.getConfirmationDialog(confirmationText)) {
                    null -> closeApp()
                    true -> print50Tasks(tasks, inputCtr++)
                    false -> taskMenu()
                }
            }
        }
    }

    private fun print50Tasks(tasks: List<Task>, index: Int) {
        var lastRange = (index + 1) * 50
        if (lastRange >= tasks.size)
            lastRange = tasks.size - 1
        consoleMenu.showTasks(tasks.slice(index .. lastRange))
    }
}