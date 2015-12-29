package ecarrara.eng.vilibra.domain.repository;

import ecarrara.eng.vilibra.domain.entity.BookBorrower;

/**
 *  Define an interface to provide access to {@link BookBorrower}
 */
public interface BookBorrowerRepository {

    BookBorrower bookBorrower(final String bookBorrowerId);

}
