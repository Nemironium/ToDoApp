import TestData.Companion.mockedFiles
import TestData.Companion.mockedHomeList
import TestData.Companion.mockedStudyList
import TestData.Companion.mockedWorkList
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import entities.Task
import entities.TaskStatus
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.junit.Before
import org.junit.Test
import utils.Result

@UnstableDefault
class TasksTests {
    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Before
    fun init() {
        filesInteractor = mock {
            on { writeListToFile(argWhere { it in mockedFiles }, any()) }
                .doReturn(Result.success(Unit))
            on { readFile(argWhere { it in mockedFiles }) }
                .doReturn(Result.success(mockedHomeList))
                .doReturn(Result.success(mockedWorkList))
                .doReturn(Result.success(mockedStudyList))
        }
        listInteractor = ToDoInteractor(filesInteractor)
    }

    @Test
    fun addTasksToNewList() {
        initNewListInteractor()
        listInteractor.createNewList("Home2")
        assertThat(listInteractor.showTasks()).isEmpty()

        val task = Task(name = "Feed puppy", description = "Do not forget meat!!!")
        listInteractor.addTask(task)
        assertThat(listInteractor.showTasks()?.size).isEqualTo(1)
        assertThat(listInteractor.showTasks()?.get(0)?.name).isEqualTo(task.name)
    }

    private fun initNewListInteractor() {
        filesInteractor = mock {
            on { createNewFile(argWhere { it !in mockedFiles }, any()) }
                .doReturn(Result.success(Unit))
            on { writeListToFile(any(), any()) }
                .doReturn(Result.success(Unit))
        }
        listInteractor = ToDoInteractor(filesInteractor)
    }

    @Test
    fun addTasksToExistingList() {
        listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedHomeList.title)
        val task1 = Task(name = "Feed kitty", description = "Do not forget milk!!!")

        listInteractor.addTask(task1)
        assertThat(listInteractor.showTasks()?.find { it.name == task1.name }).isNotNull()

        listInteractor.selectCurrentList(mockedWorkList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedWorkList.title)
        val task2 = Task(name = "Meeting with CEO")

        listInteractor.addTask(task2)
        assertThat(listInteractor.showTasks()?.find { it.name == task2.name }).isNotNull()
    }

    @Test
    fun addDoneTaskToExistingList() {
        listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedHomeList.title)

        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(3)
        val task1 = Task(name = "Feed kitty", description = "Do not forget milk!!!",
            status = TaskStatus.DONE.code)
        listInteractor.addTask(task1)
        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(3)
    }

    @Test
    fun setDoneExistingTask() {
        listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedHomeList.title)

        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(3)
        val result = listInteractor.setTaskAsDone(mockedHomeList.tasks[0].id)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(2)
    }

    @Test
    fun setDoneNotExistingTask() {
        /* because of modifying mockedHomeList and mockedWorkList in addTasksToExistingList */
        listInteractor.selectCurrentList(mockedHomeList.title)
        listInteractor.selectCurrentList(mockedWorkList.title)
        listInteractor.selectCurrentList(mockedStudyList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedStudyList.title)

        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(3)
        println(listInteractor.showTasks())
        val result = listInteractor.setTaskAsDone(taskId = 4)
        assertThat(result.isFailed).isTrue()
        assertThat(listInteractor.showNotDoneTasks()?.size).isEqualTo(3)
    }

    @Test
    fun deleteExistingTask() {
        listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedHomeList.title)

        val deletedTaskId = mockedHomeList.tasks[0].id
        assertThat(listInteractor.showTasks()?.size).isEqualTo(4)
        val result = listInteractor.deleteTask(deletedTaskId)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.showTasks()?.size).isEqualTo(3)
        assertThat(listInteractor.showTasks()?.find { it.id == deletedTaskId }).isNull()
    }

    @Test
    fun deleteNotExistingTask() {
        listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(listInteractor.listName).isEqualTo(mockedHomeList.title)

        assertThat(listInteractor.showTasks()?.size).isEqualTo(4)
        val result = listInteractor.deleteTask(taskId = 5)
        assertThat(result.isFailed).isTrue()
        assertThat(listInteractor.showTasks()?.size).isEqualTo(4)
    }
}