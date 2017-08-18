package br.eng.ecarrara.vilibra.data.repository;

import br.eng.ecarrara.vilibra.data.mapper.BookRestApiJsonMapper;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.book.domain.BookRepository;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksService;

/**
 * Concrete implementation of {@link BookRepository} that connects to REST API
 * to retrieve Book Information.
 *
 * In this case we are connecting to the Google Books API.
 *
 */
public class BookRestApiRepository implements BookRepository {

    @Override public Book byIsbn(String isbn) {
        GoogleBooksService googleBooksService = new GoogleBooksService();
        JsonBookVolume returnedJsonBookVolume = googleBooksService.lookForVolumeByISBN(isbn);

        BookRestApiJsonMapper bookRestApiJsonMapper = new BookRestApiJsonMapper();
        Book book = bookRestApiJsonMapper.transform(returnedJsonBookVolume);

        return book;
    }

    @Override public void add(Book book) {
        throw new UnsupportedOperationException("The cloud service is read only for Book data.");
    }
}