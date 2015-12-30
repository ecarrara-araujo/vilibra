package ecarrara.eng.vilibra.domain.usecase;

import java.util.List;

import ecarrara.eng.vilibra.ServiceLocator;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.error.DefaultError;
import ecarrara.eng.vilibra.domain.executor.Executor;
import ecarrara.eng.vilibra.domain.executor.Interactor;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;

/**
 * {@link ListBookBorrowings} implements the domain side flow for listing all the books
 * that were borrowed using {@link BookBorrowing}.
 *
 */
public class ListBookBorrowings extends Interactor<List<BookBorrowing>> {

    private BookBorrowingRepository bookBorrowingRepository;

    public ListBookBorrowings(Executor executor, BookBorrowingRepository bookBorrowingRepository) {
        super(executor);
        this.bookBorrowingRepository = bookBorrowingRepository;
    }

    @Override public void operation() {
        List<BookBorrowing> borrowedBooks =  this.bookBorrowingRepository.borrowedBooks();
        if(borrowedBooks != null) {
            notifyFinished(borrowedBooks);
        } else {
            notifyError(new DefaultError(null));
        }
    }
}
