package ecarrara.eng.vilibra.data.repository;

import ecarrara.eng.vilibra.data.mapper.BookRestApiJsonMapper;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.domain.repository.BookRepository;
import ecarrara.eng.vilibra.model.BookVolume;
import ecarrara.eng.vilibra.service.GoogleBooksService;

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
        BookVolume returnedBookVolume = googleBooksService.lookForVolumeByISBN(isbn);

        BookRestApiJsonMapper bookRestApiJsonMapper = new BookRestApiJsonMapper();
        Book book = bookRestApiJsonMapper.transform(returnedBookVolume);

        return book;
    }

    @Override public void add(Book book) {
        throw new UnsupportedOperationException("The cloud service is read only for Book data.");
    }
}