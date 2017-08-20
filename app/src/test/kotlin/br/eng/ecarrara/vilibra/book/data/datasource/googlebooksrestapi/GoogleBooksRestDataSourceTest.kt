package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolumeCollection
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.testutils.extension.fromJson
import br.eng.ecarrara.vilibra.testutils.extension.getResource
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GoogleBooksRestDataSourceTest(private val inputResource: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: input json: {0}")
        fun data() = listOf("json/book/gbooks_searchVolumeData_returns_dominandoAndroid.json")
    }

    @Test
    fun searchForBookBy_whenCalled_withValidISBN_returnsBookSuccessfully() {

        // Arrange
        val fakeISBN = "8575224123"
        val (bookCollectionToBeReturned, expectedBook) = getInputAndExpectedDataFrom(inputResource)
        val googleBooksRestApi: GoogleBooksRestApi = mock {
            on { searchVolumeData(any(), any()) }.doReturn( Single.just(bookCollectionToBeReturned))
        }

        // Act and Assert
        GoogleBooksRestDataSource(googleBooksRestApi)
                .searchForBookBy(fakeISBN)
                .test()
                .assertValue(expectedBook)
                .assertComplete()

    }

    private fun getInputAndExpectedDataFrom(resource: String) : Pair<JsonBookVolumeCollection, Book> {
        val jsonBookCollection: JsonBookVolumeCollection = Gson().fromJson(getResource(resource))
        return jsonBookCollection to jsonBookCollection.items[0].toBook()
    }

}