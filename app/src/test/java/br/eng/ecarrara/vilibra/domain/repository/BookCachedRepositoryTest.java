package br.eng.ecarrara.vilibra.domain.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import br.eng.ecarrara.vilibra.BuildConfig;
import br.eng.ecarrara.vilibra.book.data.BookCachedRepository;
import br.eng.ecarrara.vilibra.book.data.datasource.BookLocalCache;
import br.eng.ecarrara.vilibra.book.data.datasource.BookRemoteDataSource;
import br.eng.ecarrara.vilibra.book.domain.BookRepository;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.fixture.BookFixture;
import io.reactivex.Single;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BookCachedRepositoryTest {

    private static final String INVALID_ISBN = "";

    private BookRemoteDataSource bookRemoteDataSource;
    private BookLocalCache cache;
    private Book testBook;

    @Before
    public void prepareMocks() {
        this.testBook = BookFixture.INSTANCE.getTestBookDevsTestBook();
        this.bookRemoteDataSource = mock(BookRemoteDataSource.class);
        this.cache = mock(BookLocalCache.class);
    }

    @Test
    public void testBookByIsbnExistingInCache() throws Exception {

        when(this.cache.get(this.testBook.getIsbn10())).thenReturn(Single.just(testBook));

        BookCachedRepository bookCachedRepository =
                new BookCachedRepository(this.bookRemoteDataSource, this.cache);

        bookCachedRepository
                .byIsbn(this.testBook.getIsbn10())
                .test()
                .assertComplete()
                .assertValue(testBook);

    }

    @Test
    public void testBookByIsbnWithCacheMissAndIsbn10AsCacheKey() {
        Book mockBook = mock(Book.class);
        when(mockBook.getIsbn10()).thenReturn("VALID_ISBN");
        when(mockBook.getIsbn13()).thenReturn(INVALID_ISBN);

        testBookByIsbnWithCacheMiss(mockBook);
    }

    @Test
    public void testBookByIsbnWithCacheMissAndIsbn13AsCacheKey() {

        Book mockBook = mock(Book.class);
        when(mockBook.getIsbn10()).thenReturn(INVALID_ISBN);
        when(mockBook.getIsbn13()).thenReturn("VALID_ISBN");

        testBookByIsbnWithCacheMiss(mockBook);
    }

    @Test
    public void testBookIsbnInvalidCacheKeyCondition() {
        Book invalidCacheKeyBook = mock(Book.class);
        when(invalidCacheKeyBook.getIsbn10()).thenReturn(INVALID_ISBN);
        when(invalidCacheKeyBook.getIsbn13()).thenReturn(INVALID_ISBN);

        when(this.cache.get(INVALID_ISBN))
                .thenReturn(Single.just(Book.Companion.getNO_BOOK()));
        when(this.bookRemoteDataSource.searchForBookBy(INVALID_ISBN))
                .thenReturn(Single.just(invalidCacheKeyBook));

        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRemoteDataSource, this.cache);

        bookCachedRepository
                .byIsbn(INVALID_ISBN)
                .test()
                .assertComplete()
                .assertValue(Book.Companion.getNO_BOOK());

        bookCachedRepository
                .byIsbn(INVALID_ISBN)
                .test()
                .assertComplete()
                .assertValue(Book.Companion.getNO_BOOK());

    }

    @Test
    public void testAdd() throws Exception {
        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRemoteDataSource, this.cache);

        bookCachedRepository
                .add(this.testBook)
                .andThen(bookCachedRepository.byIsbn(testBook.getIsbn10()))
                .test()
                .assertComplete()
                .assertValue(testBook);
    }

    public void testBookByIsbnWithCacheMiss(Book book) {

        when(this.cache.get(this.testBook.getIsbn10()))
                .thenReturn(Single.just(Book.Companion.getNO_BOOK()));
        when(this.bookRemoteDataSource.searchForBookBy(this.testBook.getIsbn10()))
                .thenReturn(Single.just(book));

        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRemoteDataSource, this.cache);

        bookCachedRepository
                .byIsbn(this.testBook.getIsbn10())
                .test()
                .assertComplete()
                .assertValue(book);

    }

}