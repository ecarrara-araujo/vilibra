package br.eng.ecarrara.vilibra.book.data.datasource.contentprovider

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import br.eng.ecarrara.vilibra.book.data.datasource.BookLocalCache
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.core.data.RxCache
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract.BookEntry
import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.mapper.BookContentProviderMapper
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Concrete implementation of [RxCache] that connects to an Android Content Provider
 * and use it as a cache to store and retrieve Book Information.
 *
 * For convenience we use a String as key type to be possible to use the books ISBN as key values.
 *
 */
class BookContentProviderCache
@Inject constructor(
        private val applicationContext: Context
) : BookLocalCache {

    override fun get(bookIsbn: String): Single<Book> {
        return Single.defer {
            val selectionArgs = arrayOf(bookIsbn, bookIsbn)
            val bookCursor = applicationContext.contentResolver.query(
                    BookEntry.CONTENT_URI,
                    BOOK_COLUMNS, BOOK_BY_ISBN_SELECTION,
                    selectionArgs, null)

            Single.just(transformBookFromCursor(bookCursor))
        }

    }

    override fun put(bookIsbn: String, bookToBeCached: Book) = Completable.defer {
        val bookMapper = BookContentProviderMapper()
        val bookContentValues = bookMapper.transform(bookToBeCached)

        // TODO: Could check here if the book id is < 0 in that case we do an insert otherwise we can try an update

        val bookUri = applicationContext.contentResolver.insert(BookEntry.CONTENT_URI, bookContentValues)
        Completable.complete()
    }

    override fun remove(bookIsbn: String) = Completable.defer {
        val selectionArgs = arrayOf(bookIsbn, bookIsbn)
        val rowsDeleted = applicationContext.contentResolver.delete(
                BookEntry.CONTENT_URI, BOOK_BY_ISBN_SELECTION, selectionArgs)
        Completable.complete()
    }

    override fun isCached(bookIsbn: String) = get(bookIsbn).map { book -> book !== Book.NO_BOOK }

    override fun clear() =
            Completable.defer {
                applicationContext.contentResolver.delete(BookEntry.CONTENT_URI, null, null)
                Completable.complete()
            }

    private fun transformBookFromCursor(cursor: Cursor?): Book {
        val bookContentProviderMapper = BookContentProviderMapper()
        var book = Book.NO_BOOK
        if (cursor != null && cursor.moveToFirst()) {
            book = bookContentProviderMapper.transform(cursor)
        }
        return book
    }

    companion object {

        private val BOOK_COLUMNS = arrayOf(BookEntry.TABLE_NAME + "." + BookEntry.COLUMN_BOOK_ID, BookEntry.COLUMN_TITLE, BookEntry.COLUMN_SUBTITLE, BookEntry.COLUMN_AUTHORS, BookEntry.COLUMN_PUBLISHER, BookEntry.COLUMN_ISBN_10, BookEntry.COLUMN_ISBN_13, BookEntry.COLUMN_PAGES, BookEntry.COLUMN_PUBLISHED_DATE)

        private val BOOK_BY_ISBN_SELECTION = StringBuilder(BookEntry.COLUMN_ISBN_10)
                .append(" = ? OR ")
                .append(BookEntry.COLUMN_ISBN_13)
                .append(" = ?")
                .toString()

    }

}
