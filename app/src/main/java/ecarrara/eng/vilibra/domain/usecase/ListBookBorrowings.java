package ecarrara.eng.vilibra.domain.usecase;

import java.util.List;

import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;

/**
 * {@link ListBookBorrowings} implements the domain side flow for listing all the books
 * that were borrowed using {@link BookBorrowing}.
 *
 */
public class ListBookBorrowings {

    private BookBorrowingRepository bookBorrowingRepository;

    public ListBookBorrowings(BookBorrowingRepository bookBorrowingRepository) {
        this.bookBorrowingRepository = bookBorrowingRepository;
    }

    public List<BookBorrowing> execute() {
        return this.bookBorrowingRepository.borrowedBooks();
    }

}
