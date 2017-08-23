package br.eng.ecarrara.vilibra.book.data.datasource.contentprovider

import android.content.Context
import br.eng.ecarrara.vilibra.BuildConfig
import br.eng.ecarrara.vilibra.book.data.datasource.BookLocalCache
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.data.VilibraContract
import br.eng.ecarrara.vilibra.data.VilibraProvider
import br.eng.ecarrara.vilibra.fakedata.BookFakeDataFactory
import br.eng.ecarrara.vilibra.fakedata.VilibraProviderFakeDataInitializer
import br.eng.ecarrara.vilibra.utils.RobolectricUtils
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowContentResolver

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class BookContentProviderCacheTest {

    private lateinit var context: Context

    private lateinit var vilibraProviderFixture: VilibraProviderFakeDataInitializer
    private lateinit var testBook: Book

    private lateinit var bookLocalCache: BookLocalCache

    @Before
    fun prepareTest() {
        this.context = RuntimeEnvironment.application!!

        ShadowContentResolver.registerProviderInternal(
                VilibraContract.CONTENT_AUTHORITY,
                Robolectric.setupContentProvider(VilibraProvider::class.java)
        )

        this.vilibraProviderFixture = VilibraProviderFakeDataInitializer(this.context)
        this.vilibraProviderFixture.prepareTestProvider()
        this.testBook = this.vilibraProviderFixture.getProAndroid4()!!

        this.bookLocalCache = BookContentProviderCache(this.context)
    }

    @After
    fun cleanUp() {
        this.vilibraProviderFixture.clearContentProviderData()
    }

    @Test
    @Throws(Exception::class)
    fun testGetWithIsbn10() {
        bookLocalCache.get(testBook.isbn10)
                .test()
                .assertValue(testBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testGetWithIsbn13() {
        bookLocalCache.get(testBook.isbn13)
                .test()
                .assertValue(testBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testPutWithIsbn10() {

        vilibraProviderFixture.clearContentProviderData()
        val anotherBook = BookFakeDataFactory.fakeBookDevsTest
        bookLocalCache.put(anotherBook.isbn10, anotherBook)
                .test()
                .assertComplete()

        bookLocalCache.get(anotherBook.isbn10)
                .test()
                .assertValue(anotherBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testPutWithIsbn13() {
        vilibraProviderFixture.clearContentProviderData()
        val anotherBook = BookFakeDataFactory.fakeBookDevsTest

        bookLocalCache.put(anotherBook.isbn13, anotherBook)
                .test()
                .assertComplete()

        bookLocalCache.get(anotherBook.isbn13)
                .test()
                .assertValue(anotherBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testPutWithIsbn10AdGetWithIsbn13() {
        vilibraProviderFixture.clearContentProviderData()
        val anotherBook = BookFakeDataFactory.fakeBookDevsTest

        bookLocalCache.put(anotherBook.isbn10, anotherBook)
                .test()
                .assertComplete()

        bookLocalCache.get(anotherBook.isbn13)
                .test()
                .assertValue(anotherBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testPutWithIsbn13AndGetWithIsbn10() {
        vilibraProviderFixture.clearContentProviderData()
        val anotherBook = BookFakeDataFactory.fakeBookDevsTest

        bookLocalCache.put(anotherBook.isbn13, anotherBook)
                .test()
                .assertComplete()

        bookLocalCache.get(anotherBook.isbn10)
                .test()
                .assertValue(anotherBook)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveWithIsbn10() {
        bookLocalCache.remove(this.testBook.isbn10)
                .test()
                .assertComplete()

        bookLocalCache.get(this.testBook.isbn10)
                .test()
                .assertValue(Book.NO_BOOK)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testRemoveWithIsbn13() {
        bookLocalCache.remove(this.testBook.isbn13)
                .test()
                .assertComplete()

        bookLocalCache.get(this.testBook.isbn10)
                .test()
                .assertValue(Book.NO_BOOK)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testIsCachedWithIsbn10() {
        bookLocalCache.isCached(testBook.isbn10)
                .test()
                .assertValue(true)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testIsCachedWithIsbn13() {
        bookLocalCache.isCached(testBook.isbn13)
                .test()
                .assertValue(true)
                .assertComplete()
    }

    @Test
    @Throws(Exception::class)
    fun testClear() {

        bookLocalCache.clear()
                .test()
                .assertComplete()

        val retrievedBooks = context.contentResolver.query(
                VilibraContract.BookEntry.CONTENT_URI,
                arrayOf(VilibraContract.BookEntry.COLUMN_BOOK_ID),
                null, null, null
        )

        val areThereAnyBooksInTheCache = retrievedBooks!!.moveToFirst()
        assertThat(areThereAnyBooksInTheCache, `is`(false))

        retrievedBooks.close()
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupRobolectric() {
            RobolectricUtils.redirectLogsToSystemOutput()
        }
    }
}
