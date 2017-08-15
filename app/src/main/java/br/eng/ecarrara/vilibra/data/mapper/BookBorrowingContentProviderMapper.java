package br.eng.ecarrara.vilibra.data.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.data.VilibraContract.LendingEntry;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrower;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowerRepository;

/**
 * Mapper class to transform a {@link Cursor} containing book borrowing information
 * from a SQL database to a {@link BookBorrowing} from the domain and vice versa.
 */
public class BookBorrowingContentProviderMapper {

    private BookContentProviderMapper bookContentProviderMapper;
    private BookBorrowerRepository bookBorrowerRepository;

    public BookBorrowingContentProviderMapper() {
        this.bookContentProviderMapper = new BookContentProviderMapper();
    }

    public List<BookBorrowing> transform(Cursor cursor) {
        List<BookBorrowing> bookBorrowings = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            do {
                long bookBorrowingId = cursor.getLong(cursor.getColumnIndex(LendingEntry.COLUMN_LENDING_ID));
                String bookBorrowerContactUri =
                        cursor.getString(cursor.getColumnIndex(LendingEntry.COLUMN_CONTACT_URI));
                Date lendingDate = VilibraContract.getDateFromDb(
                        cursor.getString(cursor.getColumnIndex(LendingEntry.COLUMN_LENDING_DATE)));

                Book borrowedBook = this.bookContentProviderMapper.transform(cursor);
                BookBorrower bookBorrower = new BookBorrower(bookBorrowerContactUri);
                BookBorrowing bookBorrowing =
                        new BookBorrowing(bookBorrowingId, borrowedBook, bookBorrower, lendingDate);

                bookBorrowings.add(bookBorrowing);
            } while (cursor.moveToNext());
        }

        return bookBorrowings;
    }

    public ContentValues transform(BookBorrowing bookBorrowing) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LendingEntry.COLUMN_BOOK_ID,
                bookBorrowing.getBorrowedBook().getId());
        contentValues.put(LendingEntry.COLUMN_CONTACT_URI,
                bookBorrowing.getBorrower().getContactInformationUri());
        contentValues.put(LendingEntry.COLUMN_LENDING_DATE,
                VilibraContract.getDbDateString(bookBorrowing.getLendingDate()));

        return contentValues;
    }

}
