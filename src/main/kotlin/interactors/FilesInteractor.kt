package interactors

import Contracts
import entities.ToDoList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonException
import utils.Result
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

    override fun createNewFile(fileName: String, newToDoList: ToDoList): Result<Unit> {
        val outFile = File(fileName)
        if (outFile.exists() && outFile.isFile)
            return Result.error("File $fileName already exists")
        return writeFile(outFile, newToDoList)
    }

    override fun deleteFile(fileName: String): Result<Unit> {
        val file = File(fileName)
        if (!file.exists())
            return Result.error("File $fileName does not exist")

        try {
            file.delete()
        } catch (e: SecurityException) {
            return Result.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Result.error("IO error. ${e.message}")
        }

        return Result.success(Unit)
    }

    override fun readFile(fileName: String): Result<ToDoList> {
        val inputFile = File(fileName)

        if (!inputFile.exists())
            return Result.error("File $fileName does not exist")

        val jsonData: String
        try {
            jsonData = inputFile.readText()
        } catch (e: SecurityException) {
            return Result.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Result.error("IO error. ${e.message}")
        }

        val parsedJson: ToDoList
        try {
            parsedJson = Json.parse(ToDoList.serializer(), jsonData)
        }  catch (e: JsonException) {
            return Result.error("Encoding error. ${e.message}")
        } catch (e: SerializationException) {
            return Result.error("Serialization error. ${e.message}")
        }

        return Result.success(parsedJson)
    }

    /* TODO (не нужно перезаписывать весь файл, а лишь дописывать новые)*/
    override fun writeListToFile(fileName: String, toDoList: ToDoList): Result<Unit> {
        val outFile = File(fileName)

        if (!outFile.exists())
            return Result.error("File $fileName does not exist")

        return writeFile(outFile, toDoList)
    }

    private fun writeFile(file: File, toDoList: ToDoList): Result<Unit> {
        val jsonData: String
        try {
            jsonData = Json.stringify(ToDoList.serializer(), toDoList)
        } catch (e: JsonException) {
            return Result.error("Encoding error. ${e.message}")
        } catch (e: SerializationException) {
            return Result.error("Serialization error. ${e.message}")
        }

        try {
            file.writeText(jsonData)
        } catch (e: SecurityException) {
            return Result.error("Security error. ${e.message}")
        } catch (e: IOException) {
            return Result.error("IO error. ${e.message}")
        }

        return Result.success(Unit)
    }
}