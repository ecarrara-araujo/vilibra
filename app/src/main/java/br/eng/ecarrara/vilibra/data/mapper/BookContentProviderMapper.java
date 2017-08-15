package br.eng.ecarrara.vilibra.data.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.domain.entity.Book;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDateFromDb;
import static br.eng.ecarrara.vilibra.data.VilibraContract.getDbDateString;

/**
 * Mapper class to transform a {@link Cursor} containing book information
 * from a SQL database to a {@link Book} from the domain.
 */
public class BookContentProviderMapper {

    public Book transform(Cursor cursor) {
        if(cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst()) {
            return Book.NO_BOOK;
        }

        long id = cursor.getLong(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ID));
        String title = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_TITLE));
        String subtitle = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUBTITLE));
        String authors = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_AUTHORS));
        String publisher = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHER));
        String publishedDate =
                cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHED_DATE));
        String isbn10 = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_10));
        String isbn13 = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_13));
        int numberOfPages = cursor.getInt(cursor.getColumnIndex(BookEntry.COLUMN_PAGES));

        Book book = new Book.Builder(id, title)
                .setSubtitle(subtitle)
                .setAuthors(AuthorsListMapper.transformAuthorsFromCommaSeparatedList(authors))
                .setPublisher(publisher)
                .setPublishedDate(getDateFromDb(publishedDate))
                .setIsbn10(isbn10)
                .setIsbn13(isbn13)
                .setPageCount(numberOfPages)
                .build();

        return book;
    }

    public ContentValues transform(Book book) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BookEntry.COLUMN_TITLE, book.getTitle());
        contentValues.put(BookEntry.COLUMN_SUBTITLE, book.getSubtitle());

        contentValues.put(BookEntry.COLUMN_AUTHORS,
                AuthorsListMapper.transformAuthorsListToDatabaseFormat(book.getAuthors()));

        contentValues.put(BookEntry.COLUMN_PUBLISHER, book.getPublisher());
        contentValues.put(BookEntry.COLUMN_PUBLISHED_DATE, getDbDateString(book.getPublishedDate()));
        contentValues.put(BookEntry.COLUMN_ISBN_10, book.getIsbn10());
        contentValues.put(BookEntry.COLUMN_ISBN_13, book.getIsbn13());
        contentValues.put(BookEntry.COLUMN_PAGES, book.getPageCount());

        return contentValues;
    }

}
