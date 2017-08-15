package br.eng.ecarrara.vilibra.fixture;

import java.util.Date;

import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrower;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDateFromDb;

public class BookBorrowingFixture {

    public static final String BASE_TEST_BOOK_BORROWING_LENDING_DATE_STRING = "19860622";
    public static final Date BASE_TEST_BOOK_BORROWING_LENDING_DATE =
            getDateFromDb(BASE_TEST_BOOK_BORROWING_LENDING_DATE_STRING);

    /**
     * Prepare a {@link BookBorrowing} with some fake data to be used with tests.
     *
     * @return
     */
    public static BookBorrowing getTestBookBorrowing() {
        Book borrowedBook = BookFixture.INSTANCE.getTestBookDevsTestBook();
        BookBorrower borrower = BookBorrowerFixture.getMasterBorrowerTestData();
        return getTestBookBorrowing(borrowedBook, borrower);
    }

    /**
     * Prepare a {@link BookBorrowing} with some fake data, uses a pre existent book instance.
     * @param book
     * @return
     */
    public static BookBorrowing getTestBookBorrowing(Book book) {
        BookBorrower borrower = BookBorrowerFixture.getMasterBorrowerTestData();
        return getTestBookBorrowing(book, borrower);
    }

    /**
     * Prepare a {@link BookBorrowing} with some fake data, uses a pre existent
     * book and borrower instances.
     * @param book
     * @param bookBorrower
     * @return
     */
    public static BookBorrowing getTestBookBorrowing(Book book, BookBorrower bookBorrower) {
        BookBorrowing bookBorrowing = new BookBorrowing(
                BookBorrowing.NOT_PERSISTED_ID,
                book,
                bookBorrower,
                BASE_TEST_BOOK_BORROWING_LENDING_DATE);

        return bookBorrowing;
    }

}
