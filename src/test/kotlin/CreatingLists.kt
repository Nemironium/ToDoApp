import TestData.Companion.mockedEmptyHome2List
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.junit.Test
import utils.Result
import utils.Status
import utils.toListFileName

@UnstableDefault
class CreatingLists {

    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Test
    fun createExistingList() {
        filesInteractor = mock {
            on { createNewFile(eq("Home".toListFileName()), any()) }
                .doReturn(Result.error("File Home_todo-list.json already exists"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val result = listInteractor.createNewList("Home")
        assertThat(result.status).isEqualTo(Status.ERROR)
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
        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(listInteractor.listName).isEqualTo(mockedEmptyHome2List.title)
    }
}