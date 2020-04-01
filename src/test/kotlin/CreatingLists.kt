import TestUtils.Companion.mockedEmptyHome2List
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.junit.Before
import org.junit.Test
import utils.Result
import utils.Status
import utils.toListFileName

@UnstableDefault
class CreatingLists {

    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Before
    fun init() {
        filesInteractor = mock {
            on { createNewFile(eq("Home".toListFileName()), any()) }
                .doReturn(Result.error("File Home_todo-list.json already exists"))
            on { createNewFile(eq("Study".toListFileName()), any()) }
                .doReturn(Result.error("File Study_todo-list.json already exists"))
            on { createNewFile(eq("Work".toListFileName()), any()) }
                .doReturn(Result.error("File Work_todo-list.json already exists"))
            on { createNewFile(mockedEmptyHome2List.title.toListFileName(), mockedEmptyHome2List) }
                .doReturn(Result.success(Unit))
        }
        listInteractor = ToDoInteractor(filesInteractor)
    }

    @Test
    fun createExistingList() {
        val result = listInteractor.createNewList("Home")
        assertThat(result.status).isEqualTo(Status.ERROR)
        assertThat(listInteractor.listName).isNull()
    }

    @Test
    fun verifyCreateList() {
        val result = listInteractor.createNewList("Home2")
        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(listInteractor.listName).isEqualTo("Home2")
    }
}