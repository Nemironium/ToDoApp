package console

import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.output.TermUi.confirm
import com.github.ajalt.clikt.output.TermUi.echo
import com.github.ajalt.mordant.AnsiCode
import com.github.ajalt.mordant.TermColors
import entities.Task
import entities.TaskStatus

class ConsoleMenu(private val terminalColors: TermColors)
    : ConsoleContract.ListMenu, ConsoleContract.TaskMenu {

    private val boldStyle: AnsiCode
        get() = terminalColors.bold + terminalColors.underline

    private val errorStyle: AnsiCode
        get() = terminalColors.bold + terminalColors.red

    private val strikethroughStyle: AnsiCode
        get() = terminalColors.strikethrough

    private val selectTaskMenuText: String
        get() {
            var rowNum = 0
            return "\n${++rowNum}. Select task by number" +
                    "\n${++rowNum}. Select task by keyword"
        }

    override fun getListMenuValue(isShowSelectList: Boolean): Int? {
        echo(listMenuText(isShowSelectList))
        val validNumbers = if (isShowSelectList) 1..3 else 1..1
        return enterNumberPrompt(validNumbers)
    }

    override fun getNewName(text: String, default: String?): String? =
        TermUi.prompt(text = "Enter $text", default = default) { return@prompt it }

    override fun getSelectedValueFromList(entities: List<String>, text: String): String? {
        echo("")
        var num = 0
        entities.forEach {
            echo("${++num}. $it")
        }
        val selectedList = enterNumberPrompt(1..num)
        return if (selectedList != null) entities[selectedList-1] else null
    }

    override fun printSelectedList(item: String) =
        echo(boldStyle("\nNow you on $item list"))

    override fun showError(message: String?) =
        echo(errorStyle("$message"))

    override fun getConfirmationDialog(text: String): Boolean?
            = confirm(text, default = true)

    override fun getTaskMenuValue(isShowSelectTask: Boolean?): Int? {
        echo(taskMenuText(isShowSelectTask))
        val validNumbers = if (isShowSelectTask == true) 1..5 else 1..1
        return enterNumberPrompt(validNumbers)
    }

    override fun showTasks(tasks: List<Task>) {
        tasks.forEach {
            echo("-".repeat(80))
            echo("${boldStyle("number")}: ${it.id}")
            echo("title: ${it.name}")
            if (it.description.isNotBlank())
                echo("description: ${it.description}")
            when(it.status) {
                TaskStatus.TODO.code -> echo("status: to do")
                TaskStatus.DONE.code -> echo(strikethroughStyle("status: done"))
            }
        }
    }

    override fun getSelectedTaskMenuValue(): Int? {
        echo(selectTaskMenuText)
        return enterNumberPrompt(1..2)
    }

    override fun getTaskId(maxId: Int?): Int? = if (maxId != null)
        enterNumberPrompt(1..maxId)
    else
        0

    private fun taskMenuText(isShowSelectTask: Boolean?): String {
        var rowNum = 0
        var menu = "\n${++rowNum}. new-task"

        if (isShowSelectTask == true) {
            menu += "\n${++rowNum}. delete-task"
            menu += "\n${++rowNum}. set-done-task"
            menu += "\n${++rowNum}. show-all-tasks"
            menu += "\n${++rowNum}. show-todo-tasks"
        }
        return menu
    }

    private fun listMenuText(isShowSelectList: Boolean): String {
        var rowNum = 0
        var menu = "\n${++rowNum}. new-list"

        if (isShowSelectList) {
            menu += "\n${++rowNum}. delete-list"
            menu += "\n${++rowNum}. choose-list"
        }
        return menu
    }

    private fun enterNumberPrompt(validNumbers: IntRange): Int? {
        return TermUi.prompt("Enter a number") {
            val choice = it.toIntOrNull() ?: throw UsageError("$it is not a valid number")
            if (choice !in validNumbers)
                throw UsageError("$it is not a valid number")
            return@prompt choice
        }
    }
}