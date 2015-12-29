package ecarrara.eng.vilibra.domain.repository;

import java.util.List;

import ecarrara.eng.vilibra.domain.entity.Book;

/**
 * Defines an interface to provide access to a repository of {@link Book}
 */
public interface BookRepository {

    Book byIsbn(final String isbn);
    void add(final Book book);

}
