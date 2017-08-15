package br.eng.ecarrara.vilibra.fixture

import android.content.ContentUris
import android.content.Context
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.data.VilibraContract
import br.eng.ecarrara.vilibra.data.mapper.BookBorrowingContentProviderMapper
import br.eng.ecarrara.vilibra.data.mapper.BookContentProviderMapper
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing
import java.security.InvalidParameterException

class VilibraProviderFixture(private val context: Context?) {
    private val bookContentProviderMapper: BookContentProviderMapper
    private val bookBorrowingContentProviderMapper: BookBorrowingContentProviderMapper

    private var devsTestBookBorrowing: BookBorrowing? = null
    private var devsTestBook: Book? = null
    private var dominandoAndroid: Book? = null
    private var proAndroid4: Book? = null

    init {
        if (context == null) {
            throw InvalidParameterException("Context cannot be null.")
        }
        this.bookContentProviderMapper = BookContentProviderMapper()
        this.bookBorrowingContentProviderMapper = BookBorrowingContentProviderMapper()
    }

    fun getDevsTestBookBorrowing(): BookBorrowing? {
        if (this.devsTestBook == null) {
            insertDevsTestBookWithBorrowing()
        }
        return this.devsTestBookBorrowing
    }

    fun getDevsTestBook(): Book? {
        if (this.devsTestBook == null) {
            insertDevsTestBookWithBorrowing()
        }
        return this.devsTestBook
    }

    fun getDominandoAndroid(): Book? {
        if (this.dominandoAndroid == null) {
            insertDominandoAndroidTestBook()
        }
        return this.dominandoAndroid
    }

    fun getProAndroid4(): Book? {
        if (this.proAndroid4 == null) {
            insertProAndroid4TestBook()
        }
        return this.proAndroid4
    }

    fun prepareTestProvider() {
        insertDevsTestBookWithBorrowing()
        insertDominandoAndroidTestBook()
        insertProAndroid4TestBook()
    }

    fun insertDevsTestBookWithBorrowing() {
        this.devsTestBook = BookFixture.testBookDevsTestBook
        val testBookContentValues = bookContentProviderMapper.transform(this.devsTestBook)
        val insertedBook = context?.getContentResolver()?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
        val insertedBookId = ContentUris.parseId(insertedBook)
        this.devsTestBook = this.devsTestBook?.copy(id = insertedBookId)

        this.devsTestBookBorrowing = BookBorrowingFixture.getTestBookBorrowing(this.devsTestBook)
        val testBorrowingContentValues = bookBorrowingContentProviderMapper.transform(this.devsTestBookBorrowing)
        val insertedBookBorrowing = context?.getContentResolver()?.
                insert(VilibraContract.LendingEntry.CONTENT_URI, testBorrowingContentValues)
        val insertedBookBorrowingId = ContentUris.parseId(insertedBookBorrowing)
        this.devsTestBookBorrowing!!.id = insertedBookBorrowingId
    }

    fun insertDominandoAndroidTestBook() {
        this.dominandoAndroid = BookFixture.testBookDominandoAndroid
        val testBookContentValues = bookContentProviderMapper.transform(this.dominandoAndroid)
        val insertedBook = context?.getContentResolver()?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
    }

    fun insertProAndroid4TestBook() {
        this.proAndroid4 = BookFixture.testBookProAndroid4
        val testBookContentValues = bookContentProviderMapper.transform(this.proAndroid4)
        val insertedBook = context?.getContentResolver()?.
                insert(VilibraContract.BookEntry.CONTENT_URI, testBookContentValues)
    }

    fun clearVilibraDatabase() {
        context?.getContentResolver()?.delete(VilibraContract.LendingEntry.CONTENT_URI, null, null)
        context?.getContentResolver()?.delete(VilibraContract.BookEntry.CONTENT_URI, null, null)

        this.devsTestBookBorrowing = null
        this.devsTestBook = null
        this.dominandoAndroid = null
        this.proAndroid4 = null
    }

}
