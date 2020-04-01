import TestData.Companion.homeTasks
import TestData.Companion.mockedFiles
import TestData.Companion.mockedHomeList
import TestData.Companion.mockedStudyList
import TestData.Companion.mockedWorkList
import TestData.Companion.studyTasks
import TestData.Companion.workTasks
import com.google.common.truth.Truth.assertThat
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
class SelectingLists {

    private lateinit var filesInteractor: FilesInteractor
    private lateinit var listInteractor: ToDoInteractor

    @Test(expected = NullPointerException::class)
    fun selectNotExistingList() {
        filesInteractor = mock {
            on { readFile(argWhere { it !in mockedFiles }) }
                .doReturn(Result.error("File does not exist"))
        }
        listInteractor = ToDoInteractor(filesInteractor)

        val selectNotExistingList = listInteractor.selectCurrentList("Home2")
        assertThat(selectNotExistingList.status).isEqualTo(Status.ERROR)

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

        var result = listInteractor.selectCurrentList("Home")
        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(listInteractor.showTasks()).isEqualTo(homeTasks)

        result = listInteractor.selectCurrentList("Work")
        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(listInteractor.showTasks()).isEqualTo(workTasks)

        result = listInteractor.selectCurrentList("Study")
        assertThat(result.status).isEqualTo(Status.SUCCESS)
        assertThat(listInteractor.showTasks()).isEqualTo(studyTasks)

        // should throw exception
        listInteractor.selectCurrentList("Home2")
    }


}