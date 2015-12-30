package ecarrara.eng.vilibra.domain.repository;

import android.text.TextUtils;

import ecarrara.eng.vilibra.domain.cache.Cache;
import ecarrara.eng.vilibra.domain.entity.Book;

/**
 * Concrete implementation of {@link BookRepository} that combines an implementation of
 * {@link Cache} with an implementation of {@link BookRepository}
 *
 */
public class BookCachedRepository implements BookRepository {

    private BookRepository bookRepository;
    private Cache<String, Book> bookCache;

    public BookCachedRepository(BookRepository bookRepository, Cache<String, Book> bookCache) {
        this.bookRepository = bookRepository;
        this.bookCache = bookCache;
    }

    @Override public Book byIsbn(String isbn) {
        Book book = this.bookCache.get(isbn);

        if(book.equals(Book.NO_BOOK)) {
            book = this.bookRepository.byIsbn(isbn);
            if(!book.equals(Book.NO_BOOK)) {
                String cacheKey = getCacheKeyForBook(book);
                if(TextUtils.isEmpty(cacheKey)) { // not possible to generate a cache key
                    book = Book.NO_BOOK;
                } else {
                    this.bookCache.put(cacheKey, book);
                }
            }
        }

        return book;
    }

    @Override public void add(Book book) {
        this.bookRepository.add(book);
    }

    private String getCacheKeyForBook(Book book) {
        String cacheKey = "";
        if(!TextUtils.isEmpty(book.getIsbn10())) {
            cacheKey = book.getIsbn10();
        } else if(!TextUtils.isEmpty(book.getIsbn13())) {
            cacheKey = book.getIsbn13();
        }
        return cacheKey;
    }
}