package br.eng.ecarrara.vilibra.domain.repository;

import br.eng.ecarrara.vilibra.domain.entity.BookBorrower;

/**
 *  Define an interface to provide access to {@link BookBorrower}
 */
public interface BookBorrowerRepository {

    BookBorrower bookBorrower(final String bookBorrowerId);

}
