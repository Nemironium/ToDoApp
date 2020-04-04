import console.ListConsole
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.system.exitProcess

@UnstableDefault
class Application : KoinComponent {

    private val interactor by inject<ToDoInteractor>()
    private val listConsole by inject<ListConsole>()

    fun initialMenu() {
        val availableLists = interactor.availableLists

        if (availableLists.size == 1) {
            val result = interactor.selectCurrentList(availableLists[0])
            if (result.isSuccessful) {
                listConsole.toTaskMenu(availableLists.first())
            } else {
                listConsole.listMenu()
            }
        } else {
            listConsole.listMenu()
        }
    }

    companion object {
        @JvmStatic
        fun closeApp(): Unit = exitProcess(-1)
    }
}