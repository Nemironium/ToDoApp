import console.ConsoleMenu
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.system.exitProcess

@UnstableDefault
class Application : KoinComponent {

    private val interactor by inject<ToDoInteractor>()
    private val consoleMenu by inject<ConsoleMenu>()

    fun initialMenu() {
        when (consoleMenu.getListMenuValue(interactor.availableLists.isNotEmpty())) {
            null -> exitProcess(-1)
            1 -> processListCreating()
            2 -> processListDeleting()
            3 -> processListChoosing()
        }
    }

    private fun processListCreating() {
        val listName = consoleMenu.getNewListName()

        if (listName != null) {
            val result = interactor.createNewList(listName)

            if (result.isSuccessful) {
                consoleMenu.printSelectedList(listName)
                taskMenu(consoleMenu.getTaskMenuValue(false))
            } else {
                consoleMenu.showError(result.message)
                initialMenu()
            }
        } else {
            exitProcess(-1)
        }
    }

    private fun processListDeleting() {
        val listName = consoleMenu.getSelectedListName(interactor.availableLists)

        if (listName != null) {
            val confirmation = consoleMenu.showDeleteConfirmation(listName)
            if (confirmation == true) {
                val result = interactor.deleteList(listName)
                if (result.isSuccessful) {
                    initialMenu()
                } else {
                    consoleMenu.showError(result.message)
                    initialMenu()
                }
            }
        } else {
            exitProcess(-1)
        }
    }

    private fun processListChoosing() {
        val listName = consoleMenu.getSelectedListName(interactor.availableLists)

        if (listName != null) {
            val result = interactor.selectCurrentList(listName)
            if (result.isSuccessful) {
                consoleMenu.printSelectedList(listName)
                taskMenu(consoleMenu.getTaskMenuValue(interactor.isTasksAvailable))
            } else {
                consoleMenu.showError(result.message)
                initialMenu()
            }
        } else {
            exitProcess(-1)
        }
    }

    private fun taskMenu(menuChoice: Int?) {
        when(menuChoice) {
            null -> exitProcess(-1)
            1 -> processTaskCreating()
            2 -> processTaskChoosing()
            3 -> processShowAllTasks()
            4 -> processShowTodoTasks()
        }
    }

    private fun processTaskCreating() {

    }

    private fun processTaskChoosing() {

    }

    private fun processShowAllTasks() {

    }

    private fun processShowTodoTasks() {

    }





}