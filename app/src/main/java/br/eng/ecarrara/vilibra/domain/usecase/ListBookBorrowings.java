package br.eng.ecarrara.vilibra.domain.usecase;

import java.util.List;

import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.domain.error.DefaultError;
import br.eng.ecarrara.vilibra.domain.executor.Executor;
import br.eng.ecarrara.vilibra.domain.executor.Interactor;
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository;

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
