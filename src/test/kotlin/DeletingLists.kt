import TestData.Companion.mockedFiles
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.argWhere
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import interactors.FilesInteractor
import interactors.ToDoInteractor
import kotlinx.serialization.UnstableDefault
import org.junit.Test
import utils.Result
import utils.Status

@UnstableDefault
class DeletingLists {
    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Test(expected = NullPointerException::class)
    fun deleteNotExistingList() {
        filesInteractor = mock {
            on { deleteFile(argWhere { it !in mockedFiles }) }
                .doReturn(Result.error("File does not exist"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val deleteNotExistingList = listInteractor.deleteList("Home2")
        Truth.assertThat(deleteNotExistingList.status).isEqualTo(Status.ERROR)

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

        var result = listInteractor.deleteList("Home")
        Truth.assertThat(result.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(listInteractor.listName).isNull()

        result = listInteractor.deleteList("Study")
        Truth.assertThat(result.status).isEqualTo(Status.SUCCESS)
        Truth.assertThat(listInteractor.listName).isNull()

        // should throw exception
        listInteractor.deleteList("Home2")
    }
}