package br.eng.ecarrara.vilibra.data;

import android.content.ContentValues;

import br.eng.ecarrara.vilibra.book.domain.entity.Book;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDbDateString;
import static br.eng.ecarrara.vilibra.data.mapper.AuthorsListMapper.transformAuthorsListToDatabaseFormat;

public class VilibraContentValuesBuilder {

    public static ContentValues buildFor(Book book) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_10,
                book.getIsbn10());
        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_13,
                book.getIsbn13());
        contentValues.put(VilibraContract.BookEntry.COLUMN_TITLE,
                book.getTitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_SUBTITLE,
                book.getSubtitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_AUTHORS,
                transformAuthorsListToDatabaseFormat(book.getAuthors()));
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHER,
                book.getPublisher());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHED_DATE,
                book.getPublishedDate());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PAGES,
                book.getPageCount());

        return contentValues;
    }

}
