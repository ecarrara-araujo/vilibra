package br.eng.ecarrara.vilibra.fakedata

import android.content.ContentUris
import android.content.Context
import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.mapper.BookContentProviderMapper
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.data.VilibraContract
import br.eng.ecarrara.vilibra.data.mapper.BookBorrowingContentProviderMapper
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing

class VilibraProviderFakeDataInitializer(private val context: Context) {

    // TODO Kill these mappers
    private val bookContentProviderMapper = BookContentProviderMapper()
    private val bookBorrowingContentProviderMapper = BookBorrowingContentProviderMapper()

    private var devsTestBookBorrowing: BookBorrowing? = null

    private var devsTestBook: Book? = null

    private var dominandoAndroid: Book? = null

    private var proAndroid4: Book? = null

    fun getDevsTestBookBorrowing(): BookBorrowing? {
        if (this.devsTestBook == null) {
            prepareFakeBookDevsTestWithFakeBorrowing()
        }
        return this.devsTestBookBorrowing
    }

    fun getDevsTestBook(): Book? {
        if (this.devsTestBook == null) {
            prepareFakeBookDevsTestWithFakeBorrowing()
        }
        return this.devsTestBook
    }

    fun getDominandoAndroid(): Book? {
        if (this.dominandoAndroid == null) {
            prepareFakeBookDominandoAndroid()
        }
        return this.dominandoAndroid
    }

    fun getProAndroid4(): Book? {
        if (this.proAndroid4 == null) {
            prepareFakeBookProAndroid4()
        }
        return this.proAndroid4
    }

    fun prepareTestProvider() {
        prepareFakeBookDevsTestWithFakeBorrowing()
        prepareFakeBookDominandoAndroid()
        prepareFakeBookProAndroid4()
    }

    fun prepareFakeBookDevsTestWithFakeBorrowing() {
        this.devsTestBook = BookFakeDataFactory.fakeBookDevsTest
        val testBookContentValues = bookContentProviderMapper.transform(this.devsTestBook!!)
        val insertedBook = context.contentResolver?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
        val insertedBookId = ContentUris.parseId(insertedBook)
        this.devsTestBook = this.devsTestBook?.copy(id = insertedBookId)

        this.devsTestBookBorrowing = BookBorrowingFixture.getTestBookBorrowing(this.devsTestBook)
        val testBorrowingContentValues = bookBorrowingContentProviderMapper.transform(this.devsTestBookBorrowing)
        val insertedBookBorrowing = context.getContentResolver()?.
                insert(VilibraContract.LendingEntry.CONTENT_URI, testBorrowingContentValues)
        val insertedBookBorrowingId = ContentUris.parseId(insertedBookBorrowing)
        this.devsTestBookBorrowing!!.id = insertedBookBorrowingId
    }

    fun prepareFakeBookDominandoAndroid() {
        this.dominandoAndroid = BookFakeDataFactory.fakeBookDominandoAndroid
        val testBookContentValues = bookContentProviderMapper.transform(this.dominandoAndroid!!)
        val insertedBook = context.getContentResolver()?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
        val insertedBookId = ContentUris.parseId(insertedBook)
        this.dominandoAndroid = dominandoAndroid!!.copy(id = insertedBookId)
    }

    fun prepareFakeBookProAndroid4() {
        this.proAndroid4 = BookFakeDataFactory.fakeBookProAndroid4
        val testBookContentValues = bookContentProviderMapper.transform(this.proAndroid4!!)
        val insertedBook = context.getContentResolver()?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
        val insertedBookId = ContentUris.parseId(insertedBook)
        this.proAndroid4 = proAndroid4!!.copy(id = insertedBookId)
    }

    fun clearContentProviderData() {
        context.getContentResolver()?.delete(VilibraContract.LendingEntry.CONTENT_URI, null, null)
        context.getContentResolver()?.delete(VilibraContract.BookEntry.CONTENT_URI, null, null)

        this.devsTestBookBorrowing = null
        this.devsTestBook = null
        this.dominandoAndroid = null
        this.proAndroid4 = null
    }

}
