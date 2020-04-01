import di.applicationModule
import entities.Task
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.koin.core.context.startKoin


@UnstableDefault
fun main()
{
    startKoin {
        printLogger()
        modules(applicationModule)
    }
    /*val filesInteractor = interactors.FilesInteractor()
    val files = filesInteractor.findFiles()
    *//*with(TermColors()) {
        files.forEach { println(red(it.toString())) }
        val jsonContent = filesInteractor.readFile(files[0].absolutePath)
        if (!jsonContent.isNullOrBlank()) {
            val toDoList = Json.parse(ToDoList.serializer(), jsonContent)
            println(toDoList)
        }
    }*//*

    filesInteractor.createNewFile("work")*/
    val listInteractor: ToDoInteractor

    println(FilesInteractor().findFiles())
    /*listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))*/
    /*println(listInteractor?.showTasks())
    //listInteractor?.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor?.setTaskAsDone(0)
    println(listInteractor?.showTasks())*/
}