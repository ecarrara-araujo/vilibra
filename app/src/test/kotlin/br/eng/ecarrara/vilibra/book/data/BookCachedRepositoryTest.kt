package br.eng.ecarrara.vilibra.book.data

import br.eng.ecarrara.vilibra.BuildConfig
import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource
import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.BookContentProviderCache
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.rule.RobolectricVilibraProviderRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
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
        onBookRemoteDataSourceSearchForBookBySubscribedTriggers { remoteDataSourceWasTriggered = true }

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
                .thenReturn(Single.just(fakeBook))

        // Act
        bookCachedRepository.getByIsbn(fakeBook.isbn10).test().assertComplete()

        // Assert
        bookContentProviderCache[fakeBook.isbn10]
                .test()
                .assertComplete()
                .assertValue(fakeBook)
    }

    @Test
    fun getByIsbn_whenCalled_withoutIsbnInCache_returnsBook() {
        // Arrange
        val fakeBook = getFakeBook()
        whenever(bookRemoteDataSource.searchForBookBy(fakeBook.isbn10))
                .thenReturn(Single.just(fakeBook))

        // Act & Assert
        bookCachedRepository.getByIsbn(fakeBook.isbn10)
                .test()
                .assertComplete()
                .assertValue(fakeBook)
    }

    private fun getFakeBook(): Book = robolectricVilibraProviderRule
            .vilibraProviderFakeDataInitializer
            .getDevsTestBook()!!

    private fun insertFakeBookIntoCache(): Book {
        robolectricVilibraProviderRule
                .vilibraProviderFakeDataInitializer
                .prepareFakeBookDevsTestWithFakeBorrowing()

        return robolectricVilibraProviderRule
                .vilibraProviderFakeDataInitializer
                .getDevsTestBook()!!
    }

    private fun onBookRemoteDataSourceSearchForBookBySubscribedTriggers(
            searchForBookSubscribed: () -> Unit
    ) {
        whenever(bookRemoteDataSource.searchForBookBy(any()))
                .thenReturn(Single.just(Book.NO_BOOK)
                        .doOnSubscribe { searchForBookSubscribed() })
    }

}