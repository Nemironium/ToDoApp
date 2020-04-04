package console

import Application.Companion.closeApp
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import utils.Result

@UnstableDefault
class ListConsole(
    private val consoleMenu: ConsoleMenu,
    private val interactor: ToDoInteractor,
    private val taskConsole: TaskConsole
) : Contracts.ListConsole {

    override fun listMenu() {
        when (consoleMenu.getListMenuValue(interactor.availableLists.isNotEmpty())) {
            null -> closeApp()
            1 -> processListCreating()
            2 -> processListDeleting()
            3 -> processListSelecting()
        }
    }

    override fun toTaskMenu(selectedList: String) {
        consoleMenu.printSelectedList(selectedList)
        taskConsole.taskMenu()
    }

    override fun processListCreating() {
        val listName = consoleMenu.getNewName("list name", null)

        if (listName != null) {
            val result = interactor.createNewList(listName)
            handleListSelecting(result, listName)
        } else {
            closeApp()
        }
    }

    override fun processListDeleting() {
        val listName = consoleMenu.getSelectedValueFromList(interactor.availableLists, "list")

        if (listName != null) {
            val confirmationText = "Are you sure want to delete $listName list?"
            when (consoleMenu.getConfirmationDialog(confirmationText)) {
                null -> closeApp()
                false -> listMenu()
                true -> {
                    val result = interactor.deleteList(listName)
                    if (result.isFailed)
                        consoleMenu.showError(result.message)
                    listMenu()
                }
            }
        } else {
            closeApp()
        }
    }

    override fun processListSelecting() {
        val listName = consoleMenu.getSelectedValueFromList(interactor.availableLists, "list")

        if (listName != null) {
            val result = interactor.selectCurrentList(listName)
            @Suppress("UNCHECKED_CAST")
            handleListSelecting(result as Result<Unit>, listName)
        } else {
            closeApp()
        }
    }

    private fun handleListSelecting(result: Result<Unit>, listName: String) {
        if (result.isSuccessful) {
            toTaskMenu(listName)
        } else {
            consoleMenu.showError(result.message)
            listMenu()
        }
    }
}