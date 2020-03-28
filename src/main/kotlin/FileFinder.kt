import java.io.File
import java.nio.file.FileSystems

class FileFinder {

    /* TODO(нужно сделать suspend)*/
    fun findFiles() : MutableList<File> {
        val todoFiles = mutableListOf<File>()

        val currentPath = FileSystems.getDefault().getPath(".")
            .toAbsolutePath().normalize().toString()

        File(currentPath).listFiles()?.forEach {
            if (it.nameWithoutExtension.contains("todo-list") && it.extension == "json")
                todoFiles.add(it)
        }

        return todoFiles
    }

    fun createNewToDoList(name: String) {

    }
}