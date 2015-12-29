package ecarrara.eng.vilibra.domain.entity;

import java.util.Calendar;
import java.util.Date;

/**
 * Representation of a book borrowing to someone.
 */
public class BookBorrowing {

    /**
     * Id for a book borrowing that was not persisted yet.
     */
    public static final long NOT_PERSISTED_ID = -1L;

    /**
     * Id for an empty/nonexistent book borrowing.
     */
    public static final long NONEXISTENT_ID = -2L;

    /**
     * Internal unique book borrowing identifier.
     */
    private long id;

    private final Book borrowedBook;
    private final BookBorrower borrower;
    private final Date lendingDate;

    public BookBorrowing(long id, Book borrowedBook, BookBorrower borrower, Date lendingDate) {
        this.id = id;
        this.borrowedBook = borrowedBook;
        this.borrower = borrower;
        this.lendingDate = lendingDate;
    }

    public long getId() {
        return id;
    }

    public void setId(final long newId) { this.id = newId; }

    public Book getBorrowedBook() {
        return borrowedBook;
    }

    public BookBorrower getBorrower() {
        return borrower;
    }

    public Date getLendingDate() {
        return lendingDate;
    }

    @Override public boolean equals(Object other) {
        if(other == this) {
            return true;
        }

        if(!(other instanceof BookBorrowing)) {
            return false;
        }

        BookBorrowing otherBookBorrowing = (BookBorrowing) other;
        if(this.lendingDate.equals(otherBookBorrowing.getLendingDate()) &&
                this.borrower.equals(otherBookBorrowing.getBorrower()) &&
                this.borrowedBook.equals(otherBookBorrowing.getBorrowedBook())
                ) {
            return true;
        }

        return false;
    }

}
