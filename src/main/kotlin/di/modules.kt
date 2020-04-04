package di

import com.github.ajalt.mordant.TermColors
import console.ConsoleMenu
import console.ListConsole
import console.TaskConsole
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.koin.dsl.module

@UnstableDefault
val applicationModule = module {
    single { FilesInteractor() }
    single { TermColors() }
    single { ConsoleMenu(get()) }
    single { ToDoInteractor(get()) }
    single { TaskConsole(get(), get()) }
    single { ListConsole(get(), get(), get()) }
}