package console

import entities.Task

interface Contracts {
    interface ListMenu {
        fun getListMenuValue(isShowSelectList: Boolean): Int?

        fun getNewName(text: String,  default: String?): String?

        fun getSelectedValueFromList(entities: List<String>, text: String): String?

        fun printSelectedList(item: String)

        fun showError(message: String?)

        fun getConfirmationDialog(text: String): Boolean?
    }

    interface TaskMenu {
        fun getTaskMenuValue(isShowSelectTask: Boolean?): Int?

        fun showTasks(tasks: List<Task>)

        fun getSelectedTaskMenuValue(): Int?

        fun getTaskId(maxId: Int?): Int?
    }

    interface ListConsole {
        fun listMenu()

        fun toTaskMenu(selectedList: String)

        fun processListCreating()

        fun processListDeleting()

        fun processListSelecting()
    }

    interface TaskConsole {
        fun taskMenu()

        fun processTaskCreating()

        fun processTaskDeleting()

        fun processTaskCompleting()

        fun processShowAllTasks()

        fun processShowTodoTasks()
    }

}