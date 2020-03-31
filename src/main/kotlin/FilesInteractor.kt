import entities.ToDoList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonException
import utils.Responce
import java.io.File
import java.io.IOException
import java.nio.file.FileSystems

@UnstableDefault
class FilesInteractor : Contracts.FilesInteractor {

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

    override fun createNewFile(fileName: String): Responce<Unit> {
        val outFile = File(fileName)
        if (outFile.exists() && outFile.isFile)
            return Responce.error("File $fileName already exists")

        return writeFile(outFile, ToDoList(fileName, mutableListOf()))
    }

    override fun deleteFile(fileName: String): Responce<Unit> {
        val file = File(fileName)
        if (!file.exists())
            return Responce.error("File $fileName does not exist")

        try {
            file.delete()
        } catch (e: SecurityException) {
            return Responce.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Responce.error("IO error. ${e.message}")
        }

        return Responce.success(Unit)
    }

    override fun readFile(fileName: String): Responce<ToDoList> {
        val inputFile = File(fileName)

        if (!inputFile.exists())
            return Responce.error("File $fileName does not exist")

        val jsonData: String
        try {
            jsonData = inputFile.readText()
        } catch (e: SecurityException) {
            return Responce.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Responce.error("IO error. ${e.message}")
        }

        val parsedJson: ToDoList
        try {
            parsedJson = Json.parse(ToDoList.serializer(), jsonData)
        }  catch (e: JsonException) {
            return Responce.error("Encoding error. ${e.message}")
        } catch (e: SerializationException) {
            return Responce.error("Serialization error. ${e.message}")
        }

        return Responce.success(parsedJson)
    }

    /* TODO (не нужно перезаписывать весь файл, а лишь дописывать новые)*/
    override fun writeListToFile(fileName: String, toDoList: ToDoList): Responce<Unit> {
        val outFile = File(fileName)

        if (!outFile.exists())
            return Responce.error("File $fileName does not exist")

        return writeFile(outFile, toDoList)
    }

    private fun writeFile(file: File, toDoList: ToDoList): Responce<Unit> {
        val jsonData: String
        try {
            jsonData = Json.stringify(ToDoList.serializer(), toDoList)
        } catch (e: JsonException) {
            return Responce.error("Encoding error. ${e.message}")
        } catch (e: SerializationException) {
            return Responce.error("Serialization error. ${e.message}")
        }

        try {
            file.writeText(jsonData)
        } catch (e: SecurityException) {
            return Responce.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Responce.error("IO error. ${e.message}")
        }

        return Responce.success(Unit)
    }
}