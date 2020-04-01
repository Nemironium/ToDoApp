import TestData.Companion.homeTasks
import TestData.Companion.mockedEmptyHome2List
import TestData.Companion.mockedFiles
import TestData.Companion.mockedHomeList
import TestData.Companion.mockedStudyList
import TestData.Companion.mockedWorkList
import TestData.Companion.studyTasks
import TestData.Companion.workTasks
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.junit.Test
import utils.Result
import utils.toListFileName

@UnstableDefault
class ListsTest {
    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Test
    fun createExistingList() {
        filesInteractor = mock {
            on { createNewFile(argWhere { it in mockedFiles }, any()) }
                .doReturn(Result.error("File already exists"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val result = listInteractor.createNewList("Home")
        assertThat(result.isFailed).isTrue()
        assertThat(listInteractor.listName).isNull()
    }

    @Test
    fun verifyCreateList() {
        filesInteractor = mock {
            on { createNewFile(mockedEmptyHome2List.title.toListFileName(), mockedEmptyHome2List) }
                .doReturn(Result.success(Unit))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val result = listInteractor.createNewList(mockedEmptyHome2List.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.listName).isEqualTo(mockedEmptyHome2List.title)
    }

    @Test(expected = NullPointerException::class)
    fun selectNotExistingList() {
        filesInteractor = mock {
            on { readFile(argWhere { it !in mockedFiles }) }
                .doReturn(Result.error("File does not exist"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val selectNotExistingList = listInteractor.selectCurrentList("Home2")
        assertThat(selectNotExistingList.isFailed).isTrue()

        // should throw exception
        listInteractor.selectCurrentList("Home")
    }

    @Test(expected = NullPointerException::class)
    fun selectExistingList() {
        filesInteractor = mock {
            on { readFile(argWhere { it in mockedFiles }) }
                .doReturn(Result.success(mockedHomeList))
                .doReturn(Result.success(mockedWorkList))
                .doReturn(Result.success(mockedStudyList))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        var result = listInteractor.selectCurrentList(mockedHomeList.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.showTasks()).isEqualTo(homeTasks)

        result = listInteractor.selectCurrentList(mockedWorkList.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.showTasks()).isEqualTo(workTasks)

        result = listInteractor.selectCurrentList(mockedStudyList.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.showTasks()).isEqualTo(studyTasks)

        // should throw exception
        listInteractor.selectCurrentList("Home2")
    }

    @Test(expected = NullPointerException::class)
    fun deleteNotExistingList() {
        filesInteractor = mock {
            on { deleteFile(argWhere { it !in mockedFiles }) }
                .doReturn(Result.error("File does not exist"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val result = listInteractor.deleteList("Home2")
        assertThat(result.isFailed).isTrue()

        // should throw exception
        listInteractor.deleteList("Home")
    }

    @Test(expected = NullPointerException::class)
    fun deleteExistingList() {
        filesInteractor = mock {
            on { deleteFile(argWhere { it in mockedFiles }) }
                .doReturn(Result.success(Unit))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        var result = listInteractor.deleteList(mockedHomeList.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.listName).isNull()

        result = listInteractor.deleteList(mockedStudyList.title)
        assertThat(result.isSuccessful).isTrue()
        assertThat(listInteractor.listName).isNull()

        // should throw exception
        listInteractor.deleteList("Home2")
    }
}