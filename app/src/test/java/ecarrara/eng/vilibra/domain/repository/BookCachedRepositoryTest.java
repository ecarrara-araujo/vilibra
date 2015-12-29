package ecarrara.eng.vilibra.domain.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.domain.cache.Cache;
import ecarrara.eng.vilibra.domain.entity.Book;

import static ecarrara.eng.vilibra.data.VilibraContract.getDateFromDb;
import static ecarrara.eng.vilibra.data.VilibraContract.getDbDateString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookCachedRepositoryTest {

    private static final String INVALID_ISBN = "";

    private BookRepository bookRepository;
    private Cache<String, Book> cache;
    private Book testBook;

    @Before
    public void prepareMocks() {
        this.testBook = getTestBookDevsTestBook();
        this.bookRepository = mock(BookRepository.class);
        this.cache = mock(Cache.class);
    }

    @Test
    public void testBookByIsbnExistingInCache() throws Exception {

        when(this.cache.get(this.testBook.getIsbn10())).thenReturn(this.testBook);

        BookCachedRepository bookCachedRepository =
                new BookCachedRepository(this.bookRepository, this.cache);

        Book retrievedBook = bookCachedRepository.byIsbn(this.testBook.getIsbn10());
        assertThat(retrievedBook, equalTo(this.testBook));

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

        when(this.cache.get(INVALID_ISBN)).thenReturn(Book.NO_BOOK);
        when(this.bookRepository.byIsbn(INVALID_ISBN))
                .thenReturn(invalidCacheKeyBook);

        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRepository, this.cache);

        Book retrievedBook = bookCachedRepository.byIsbn(INVALID_ISBN);
        assertThat(retrievedBook, equalTo(Book.NO_BOOK));

        retrievedBook = bookCachedRepository.byIsbn(INVALID_ISBN);
        assertThat(retrievedBook, equalTo(Book.NO_BOOK));

    }

    @Test
    public void testAdd() throws Exception {

        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRepository, this.cache);

        bookCachedRepository.add(this.testBook);

        verify(this.bookRepository).add(this.testBook);
    }

    public void testBookByIsbnWithCacheMiss(Book book) {

        when(this.cache.get(this.testBook.getIsbn10())).thenReturn(Book.NO_BOOK);
        when(this.bookRepository.byIsbn(this.testBook.getIsbn10())).thenReturn(book);

        BookCachedRepository bookCachedRepository = new
                BookCachedRepository(this.bookRepository, this.cache);

        Book retrievedBook = bookCachedRepository.byIsbn(this.testBook.getIsbn10());
        assertThat(retrievedBook, equalTo(book));

    }

    /**
     * Create a test domain Book with some fake data.
     * "Devs Test Book"
     */
    private Book getTestBookDevsTestBook() {
        // This is done to make sure that we have a compatible format with the one Vilibra uses.
        Date publishingDate = getDateFromDb(getDbDateString(Calendar.getInstance().getTime()));

        return new Book.Builder(1L, "Devs Test Book")
                .setSubtitle("A for devs to use tests to test the code with too much tests.")
                .setIsbn13("1234567890123")
                .setIsbn10("1234567890")
                .setPageCount(434)
                .setAuthors(Arrays.asList("Crazy Dev Yo", "Man of Code"))
                .setPublishedDate(publishingDate)
                .setPublisher("Herbert Richards")
                .build();
    }
}