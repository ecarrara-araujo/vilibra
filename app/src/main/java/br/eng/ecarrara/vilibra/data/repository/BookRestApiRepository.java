package br.eng.ecarrara.vilibra.data.repository;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.data.mapper.BookRestApiJsonMapper;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.domain.repository.BookRepository;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.service.GoogleBooksService;

/**
 * Concrete implementation of {@link BookRepository} that connects to REST API
 * to retrieve Book Information.
 *
 * In this case we are connecting to the Google Books API.
 *
 */
public class BookRestApiRepository implements BookRepository {

    private GoogleBooksService googleBooksService;

    @Inject
    public BookRestApiRepository(GoogleBooksService googleBooksService) {
        this.googleBooksService = googleBooksService;
    }

    @Override public Book byIsbn(String isbn) {
        BookVolume returnedBookVolume = googleBooksService.lookForVolumeByISBN(isbn);

        BookRestApiJsonMapper bookRestApiJsonMapper = new BookRestApiJsonMapper();
        Book book = bookRestApiJsonMapper.transform(returnedBookVolume);

        return book;
    }

    @Override public void add(Book book) {
        throw new UnsupportedOperationException("The cloud service is read only for Book data.");
    }
}