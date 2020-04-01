package di

import com.github.ajalt.mordant.TermColors
import console.ConsoleMenu
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
}