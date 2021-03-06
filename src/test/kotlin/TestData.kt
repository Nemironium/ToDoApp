import entities.Task
import entities.ToDoList
import utils.toListFileName

class TestData {
    companion object {
        val homeTasks = mutableListOf(
            Task(0, "Clean room tomorrow"),
            Task(1, "Cook dinner"),
            Task(2, "Feed parrot"),
            Task(3, "Wash boots", status = 1)
        )
        val studyTasks = mutableListOf(
            Task(0, "Do math", status = 1),
            Task(1, "Do physics"),
            Task(2, "English lessons"),
            Task(3, "Study programming")
        )
        val workTasks = mutableListOf(
            Task(0, "Buy coffee", status = 1),
            Task(1, "Call John after meeting"),
            Task(2, "Get to meeting"),
            Task(3, "Do new feature!!!")
        )
        val mockedHomeList = ToDoList("Home", homeTasks)
        val mockedWorkList = ToDoList("Work", workTasks)
        val mockedStudyList = ToDoList("Work", studyTasks)

        val mockedFiles = mutableListOf(
            "Home".toListFileName(),
            "Work".toListFileName(),
            "Study".toListFileName()
        )
        val mockedEmptyHome2List = ToDoList("Home2", mutableListOf())
    }
}