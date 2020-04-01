package console

import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.output.TermUi.confirm
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.mordant.TermColors

class ConsoleMenu(private val terminalColors: TermColors)
    : ConsoleContract.ListMenu, ConsoleContract.TaskMenu {

    override fun getListMenuValue(isShowSelectList: Boolean): Int? {
        echo(listMenu(isShowSelectList))

        return TermUi.prompt("Enter a number") {
            val choice = it.toIntOrNull() ?: throw UsageError("$it is not a valid integer")
            val validNumbers = if (isShowSelectList) 1..3 else 1..1
            if (choice !in validNumbers)
                throw UsageError("$it is not a valid integer")
            return@prompt choice
        }
    }

    override fun getNewListName(): String? {
        echo("")
        return TermUi.prompt("Enter list name") {
            return@prompt it
        }
    }

    override fun getSelectedListName(availableLists: List<String>): String? {
        echo("")
        var num = 0
        availableLists.forEach {
            echo("${++num}. $it")
        }
        val selectedList = TermUi.prompt("Choose a list") {
            val choice = it.toIntOrNull() ?: throw UsageError("$it is not a valid integer")
            if (choice !in 1..num)
                throw UsageError("$it is not a valid integer")
            return@prompt choice
        }
        return if (selectedList != null) availableLists[selectedList-1] else null
    }

    override fun printSelectedList(listName: String) =
        echo("Now you on $listName list")

    override fun showError(message: String?) =
        echo(terminalColors.red("$message\n"))

    override fun showDeleteConfirmation(listName: String): Boolean? =
        confirm("\nAre you sure want to delete $listName list?", default = true)

    override fun getTaskMenuValue(isShowSelectTask: Boolean?): Int? {
        echo(taskMenu(isShowSelectTask))

        return TermUi.prompt("Enter a number") {
            val choice = it.toIntOrNull() ?: throw UsageError("$it is not a valid integer")
            val validNumbers = if (isShowSelectTask == true) 1..4 else 1..3
            if (choice !in validNumbers)
                throw UsageError("$it is not a valid integer")
            return@prompt choice
        }
    }

    private fun taskMenu(isShowSelectTask: Boolean?): String {
        var rowNum = 0
        return "${++rowNum}. new-task\n" + if (isShowSelectTask == true) "${++rowNum}. choose-task\n" else "" +
                        "${++rowNum}. show-all-tasks\n${++rowNum}. show-todo-tasks"
    }

    private fun listMenu(isShowSelectList: Boolean): String =
        "1. new-list\n" + if (isShowSelectList) "2. delete-list\n3. choose-list" else ""
}