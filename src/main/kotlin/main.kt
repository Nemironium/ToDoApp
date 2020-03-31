import com.github.ajalt.mordant.TermColors
import kotlinx.serialization.UnstableDefault



@UnstableDefault
fun main()
{

    /*val filesInteractor = FilesInteractor()
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
    var listInteractor: ToDoInteractor? = null
    try {
        listInteractor = ToDoInteractor("work", FilesInteractor())
    } catch (e: Exception) {
        with(TermColors()) {
            println(red(e.message!!))
        }
    }
    println(FilesInteractor().findFiles())
    /*println(listInteractor?.showTasks())
    //listInteractor?.addTask(Task(name = "Meeting with Fred", description = "Do not forget suitcase!!!"))
    listInteractor?.setTaskAsDone(0)
    println(listInteractor?.showTasks())*/
}