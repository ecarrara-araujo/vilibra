package br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.mapper

import android.content.ContentValues
import android.database.Cursor
import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.data.VilibraContract.*

/**
 * Mapper class to transform a [Cursor] containing book information
 * from a SQL database to a [Book] from the domain.
 */
class BookContentProviderMapper {

    fun transform(cursor: Cursor?): Book {
        if (cursor == null || cursor.isAfterLast || cursor.isBeforeFirst) {
            return Book.NO_BOOK
        }

        val id = cursor.getLong(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ID))
        val title = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_TITLE))
        val subtitle = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUBTITLE))
        val authors = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_AUTHORS))
        val publisher = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHER))
        val publishedDate = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHED_DATE))
        val isbn10 = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_10))
        val isbn13 = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_13))
        val numberOfPages = cursor.getInt(cursor.getColumnIndex(BookEntry.COLUMN_PAGES))

        return Book(id, title, subtitle,
                AuthorsListMapper.transformAuthorsFromCommaSeparatedList(authors),
                publisher, getDateFromDb(publishedDate)!!, numberOfPages, isbn10, isbn13)
    }

    fun transform(book: Book): ContentValues {
        val contentValues = ContentValues()
        with(contentValues) {
            put(BookEntry.COLUMN_TITLE, book.title)
            put(BookEntry.COLUMN_SUBTITLE, book.subtitle)
            put(BookEntry.COLUMN_AUTHORS,
                    AuthorsListMapper.transformAuthorsListToDatabaseFormat(book.authors))
            put(BookEntry.COLUMN_PUBLISHER, book.publisher)
            put(BookEntry.COLUMN_PUBLISHED_DATE, getDbDateString(book.publishedDate))
            put(BookEntry.COLUMN_ISBN_10, book.isbn10)
            put(BookEntry.COLUMN_ISBN_13, book.isbn13)
            put(BookEntry.COLUMN_PAGES, book.pageCount)
        }
        return contentValues
    }

}
