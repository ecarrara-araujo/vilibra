package br.eng.ecarrara.vilibra.book.data

import br.eng.ecarrara.vilibra.BuildConfig
import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource
import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.BookContentProviderCache
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.fakedata.BookFakeDataFactory
import br.eng.ecarrara.vilibra.rule.RobolectricVilibraProviderRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.NoSuchElementException

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class BookCachedRepositoryTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @get:Rule
    val robolectricVilibraProviderRule = RobolectricVilibraProviderRule()

    @Mock
    lateinit var bookRemoteDataSource: BookRemoteDataSource

    private val bookContentProviderCache = BookContentProviderCache(RuntimeEnvironment.application)
    private lateinit var bookCachedRepository: BookCachedRepository

    @Before
    fun setUpCachedRepository() {
        bookCachedRepository = BookCachedRepository(
                bookRemoteDataSource = bookRemoteDataSource,
                bookLocalCache = bookContentProviderCache
        )
    }

    @Test
    fun getByIsbn_whenCalled_withExistingIsbnInCache_returnsCachedBook() {
        // Arrange
        val cachedFakeBook = insertFakeBookIntoCache()

        // Act & Assert
        bookCachedRepository.getByIsbn(cachedFakeBook.isbn10)
                .test()
                .assertComplete()
                .assertValue(cachedFakeBook)
    }

    @Test
    fun getByIsbn_whenCalled_withExistingIsbnInCache_neverCallsRemoteDataSource() {
        // Arrange
        val cachedFakeBook = insertFakeBookIntoCache()
        var remoteDataSourceWasTriggered = false
        onBookRemoteDataSourceSearchForBookByCompletedTriggers { remoteDataSourceWasTriggered = true }

        // Act
        bookCachedRepository.getByIsbn(cachedFakeBook.isbn10).test().assertComplete()

        // Assert No Interaction
        assertThat(remoteDataSourceWasTriggered, `is`(false))
    }

    @Test
    fun getByIsbn_whenCalled_withoutIsbnInCache_cachesBook() {
        // Arrange
        val fakeBook = getFakeBook()
        whenever(bookRemoteDataSource.searchForBookBy(fakeBook.isbn10))
                .thenReturn(Maybe.just(fakeBook))

        // Act
        bookCachedRepository.getByIsbn(fakeBook.isbn10).test().assertComplete()

        // Assert
        bookContentProviderCache[fakeBook.isbn10]
                .test()
                .assertComplete()
                .assertValue{ returnedBook -> assertSame(expected = fakeBook, real = returnedBook) }
    }

    @Test
    fun getByIsbn_whenCalled_withoutIsbnInCache_returnsBook() {
        // Arrange
        val fakeBook = getFakeBook()
        whenever(bookRemoteDataSource.searchForBookBy(fakeBook.isbn10))
                .thenReturn(Maybe.just(fakeBook))

        // Act & Assert
        bookCachedRepository.getByIsbn(fakeBook.isbn10)
                .test()
                .assertComplete()
                .assertValue{ returnedBook -> assertSame(expected = fakeBook, real = returnedBook) }
    }

    @Test
    fun getByIsbn_whenCalled_withEmptyCacheAndNoDataInRemote_throwsException() {
        // Arrange
        val fakeBook = getFakeBook()
        whenever(bookRemoteDataSource.searchForBookBy(fakeBook.isbn10))
                .thenReturn(Maybe.empty())

        // Act & Assert
        bookCachedRepository.getByIsbn(fakeBook.isbn10)
                .test()
                .assertError(NoSuchElementException::class.java)
    }

    private fun assertSame(expected: Book, real: Book)
        = expected.isbn10 == real.isbn10 &&
            expected.isbn13 == real.isbn13 &&
            expected.authors == real.authors &&
            expected.pageCount == real.pageCount &&
            expected.publishedDate == real.publishedDate &&
            expected.publisher == real.publisher &&
            expected.subtitle == real.subtitle &&
            expected.title == real.title


    private fun getFakeBook(): Book = BookFakeDataFactory.fakeBookDevsTest

    private fun insertFakeBookIntoCache(): Book {
        robolectricVilibraProviderRule
                .vilibraProviderFakeDataInitializer
                .prepareFakeBookDevsTestWithFakeBorrowing()

        return robolectricVilibraProviderRule
                .vilibraProviderFakeDataInitializer
                .getDevsTestBook()!!
    }

    private fun onBookRemoteDataSourceSearchForBookByCompletedTriggers(
            searchForBookCompleted: () -> Unit
    ) {
        whenever(bookRemoteDataSource.searchForBookBy(any()))
                .thenReturn(Maybe.just(Book.NO_BOOK)
                        .doOnComplete { searchForBookCompleted() })
    }

}