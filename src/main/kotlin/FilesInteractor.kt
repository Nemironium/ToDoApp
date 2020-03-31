import entities.ToDoList
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.FileSystems

@UnstableDefault
class FilesInteractor : Contracts.FilesInteractor {

    /* TODO(нужно сделать suspend)*/
    override fun findFiles() : MutableList<String> {
        val todoFiles = mutableListOf<String>()

        val currentPath = FileSystems.getDefault().getPath(".")
            .toAbsolutePath().normalize().toString()

        File(currentPath).listFiles()?.forEach {
            if (it.nameWithoutExtension.contains("todo-list") && it.extension == "json")
                todoFiles.add(it.nameWithoutExtension.removeSuffix("_todo-list"))
        }

        return todoFiles
    }

    override fun createNewFile(fileName: String) {
        val outFile = File(fileName)
        if (outFile.exists() && outFile.isFile)
            throw FileAlreadyExistsException(outFile, reason = "File $fileName already exists")

        val newToDoList = ToDoList(fileName, mutableListOf())
        val jsonData= Json.stringify(ToDoList.serializer(), newToDoList)

        outFile.createNewFile()
        outFile.writeText(jsonData)
    }

    override fun deleteFile(fileName: String) {
        val file = File(fileName)
        if (!file.exists())
            throw NoSuchFileException(file, reason = "File $fileName does not exist")

        file.delete()
    }

    override fun readFile(fileName: String): ToDoList? {
        val inputFile = File(fileName)

        if (!inputFile.exists())
            throw NoSuchFileException(inputFile, reason = "File $fileName does not exist")

        val jsonData = inputFile.readText()

        return Json.parse(ToDoList.serializer(), jsonData)
    }

    /* TODO (не нужно перезаписывать весь файл, а лишь дописывать новые)*/
    override fun writeListToFile(fileName: String, toDoList: ToDoList) {
        val outFile = File(fileName)

        if (!outFile.exists())
            throw NoSuchFileException(outFile, reason = "File $fileName does not exist")

        val jsonData= Json.stringify(ToDoList.serializer(), toDoList)
        outFile.writeText(jsonData)
    }
}