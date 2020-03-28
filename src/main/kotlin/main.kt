import com.github.ajalt.mordant.TermColors


fun main() {

    val fileFinder = FileFinder()
    val files = fileFinder.findFiles()
    with(TermColors()) {
        files.forEach { println(red(it.toString())) }
    }
}