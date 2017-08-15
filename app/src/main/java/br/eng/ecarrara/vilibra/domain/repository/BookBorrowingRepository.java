package br.eng.ecarrara.vilibra.domain.repository;

import java.util.List;

import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;

/**
 * Define an interface to provide access to {@link BookBorrowing}
 */
public interface BookBorrowingRepository {

    List<BookBorrowing> borrowedBooks();

    void addBookBorrowing(final BookBorrowing bookBorrowing);

    void removeBookBorrowing(final BookBorrowing bookBorrowing);

}
