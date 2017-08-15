package br.eng.ecarrara.vilibra.domain.repository;

import br.eng.ecarrara.vilibra.book.domain.entity.Book;

/**
 * Defines an interface to provide access to a repository of {@link Book}
 */
public interface BookRepository {

    Book byIsbn(final String isbn);
    void add(final Book book);

}
