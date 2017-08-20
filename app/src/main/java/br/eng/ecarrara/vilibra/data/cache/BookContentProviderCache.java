package br.eng.ecarrara.vilibra.data.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.security.InvalidParameterException;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.data.mapper.BookContentProviderMapper;
import br.eng.ecarrara.vilibra.domain.cache.Cache;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;

/**
 * Concrete implementation of {@link Cache} that connects to an Android Content Provider
 * and use it as a cache to store and retrieve Book Information.
 *
 * For convenience we use a String as key type to be possible to use the books ISBN as key values.
 *
 */
public class BookContentProviderCache implements Cache<String, Book> {

    private Context context;

    private static final String[] BOOK_COLUMNS = {
            BookEntry.TABLE_NAME + "." + BookEntry.COLUMN_BOOK_ID,
            BookEntry.COLUMN_TITLE,
            BookEntry.COLUMN_SUBTITLE,
            BookEntry.COLUMN_AUTHORS,
            BookEntry.COLUMN_PUBLISHER,
            BookEntry.COLUMN_ISBN_10,
            BookEntry.COLUMN_ISBN_13,
            BookEntry.COLUMN_PAGES,
            BookEntry.COLUMN_PUBLISHED_DATE
    };

    private final static String BOOK_BY_ISBN_SELECTION = new StringBuilder(BookEntry.COLUMN_ISBN_10)
            .append(" = ? OR ")
            .append(BookEntry.COLUMN_ISBN_13)
            .append(" = ?")
            .toString();

    @Inject
    public BookContentProviderCache(Context context) {
        if(context == null) {
            throw new InvalidParameterException();
        }
        this.context = context;
    }

    @Override public Book get(String bookIsbn) {
        final String[] selectionArgs = new String[]{bookIsbn, bookIsbn};

        Cursor bookCursor = context.getContentResolver()
                .query(BookEntry.CONTENT_URI, BOOK_COLUMNS, BOOK_BY_ISBN_SELECTION,
                        selectionArgs, null);

        Book book = transformBookFromCursor(bookCursor);
        return book;
    }

    @Override public void put(String bookIsbn, Book bookToBeCached) {
        BookContentProviderMapper bookMapper = new BookContentProviderMapper();
        ContentValues bookContentValues = bookMapper.transform(bookToBeCached);

        // TODO: Could check here if the book id is < 0 in that case we do an insert otherwise we can try an update

        Uri bookUri = context.getContentResolver().insert(BookEntry.CONTENT_URI,
                bookContentValues);
    }

    @Override public void remove(String bookIsbn) {
        final String[] selectionArgs = new String[]{bookIsbn, bookIsbn};

        int rowsDeleted = context.getContentResolver().delete(
                BookEntry.CONTENT_URI, BOOK_BY_ISBN_SELECTION, selectionArgs);
    }

    @Override public boolean isCached(String bookIsbn) {
        Book book = get(bookIsbn);
        if(book == Book.Companion.getNO_BOOK()) {
            return false;
        } else {
            return true;
        }
    }

    @Override public void clear() {
        int rowsDeleted = context.getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
    }

    private Book transformBookFromCursor(Cursor cursor) {
        BookContentProviderMapper bookContentProviderMapper = new BookContentProviderMapper();
        Book book = Book.Companion.getNO_BOOK();
        if(cursor != null && cursor.moveToFirst()) {
            book = bookContentProviderMapper.transform(cursor);
        }
        return book;
    }
}
