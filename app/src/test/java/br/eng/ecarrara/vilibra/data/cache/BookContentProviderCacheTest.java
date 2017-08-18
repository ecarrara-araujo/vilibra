package br.eng.ecarrara.vilibra.data.cache;

import android.content.Context;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.security.InvalidParameterException;

import br.eng.ecarrara.vilibra.BuildConfig;
import br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.BookContentProviderCache;
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract;
import br.eng.ecarrara.vilibra.core.data.RxCache;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.fixture.BookFixture;
import br.eng.ecarrara.vilibra.fixture.VilibraProviderFixture;
import br.eng.ecarrara.vilibra.utils.RobolectricUtils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookContentProviderCacheTest {

    private Context context;
    private VilibraProviderFixture vilibraProviderFixture;

    private Book testBook;
    private RxCache<String, Book> bookContentProviderCache;

    @BeforeClass public static void setupRobolectric() {
        RobolectricUtils.redirectLogsToSystemOutput();
        RobolectricUtils.setupVilibraContentProvider();
    }

    @Before public void prepareTest() {
        this.context = RuntimeEnvironment.application;
        this.vilibraProviderFixture = new VilibraProviderFixture(this.context);
        this.vilibraProviderFixture.prepareTestProvider();
        this.testBook = this.vilibraProviderFixture.getProAndroid4();

        this.bookContentProviderCache = new BookContentProviderCache(this.context);
    }

    @After public void cleanUp() {
        this.vilibraProviderFixture.clearVilibraDatabase();
    }

    @Test(expected = InvalidParameterException.class)
    public void testNullInitializationThrowsException() {
        this.bookContentProviderCache = new BookContentProviderCache(null);
    }

    @Test public void testGetWithIsbn10() throws Exception {
        bookContentProviderCache
                .get(this.testBook.getIsbn10())
                .test()
                .assertComplete()
                .assertValue(testBook);
    }

    @Test public void testGetWithIsbn13() throws Exception {
        bookContentProviderCache
                .get(this.testBook.getIsbn13())
                .test()
                .assertComplete()
                .assertValue(testBook);
    }

    @Test public void testPutWithIsbn10() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.INSTANCE.getTestBookDevsTestBook();

        bookContentProviderCache
                .put(anotherBook.getIsbn10(), anotherBook)
                .andThen(bookContentProviderCache.get(anotherBook.getIsbn10()))
                .test()
                .assertComplete()
                .assertValue(anotherBook);
    }

    @Test public void testPutWithIsbn13() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.INSTANCE.getTestBookDevsTestBook();

        bookContentProviderCache
                .put(anotherBook.getIsbn13(), anotherBook)
                .andThen(bookContentProviderCache.get(anotherBook.getIsbn13()))
                .test()
                .assertComplete()
                .assertValue(anotherBook);
    }

    @Test public void testPutWithIsbn10AdGetWithIsbn13() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.INSTANCE.getTestBookDevsTestBook();

        bookContentProviderCache
                .put(anotherBook.getIsbn10(), anotherBook)
                .andThen(bookContentProviderCache.get(anotherBook.getIsbn13()))
                .test()
                .assertComplete()
                .assertValue(anotherBook);
    }

    @Test public void testPutWithIsbn13AdGetWithIsbn10() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.INSTANCE.getTestBookDevsTestBook();

        bookContentProviderCache
                .put(anotherBook.getIsbn13(), anotherBook)
                .andThen(this.bookContentProviderCache.get(anotherBook.getIsbn10()))
                .test()
                .assertValue(anotherBook);
    }

    @Test public void testRemoveWithIsbn10() throws Exception {
        bookContentProviderCache
                .remove(this.testBook.getIsbn10())
                .andThen(bookContentProviderCache.get(this.testBook.getIsbn10()))
                .test()
                .assertComplete()
                .assertValue(Book.Companion.getNO_BOOK());
    }

    @Test public void testRemoveWithIsbn13() throws Exception {
        bookContentProviderCache
                .remove(this.testBook.getIsbn13())
                .andThen(bookContentProviderCache.get(this.testBook.getIsbn10()))
                .test()
                .assertComplete()
                .assertValue(Book.Companion.getNO_BOOK());
    }

    @Test public void testIsCachedWithIsbn10() throws Exception {
        bookContentProviderCache
                .isCached(this.testBook.getIsbn10())
                .test()
                .assertComplete()
                .assertValue(true);
    }

    @Test public void testIsCachedWithIsbn13() throws Exception {
        bookContentProviderCache
                .isCached(this.testBook.getIsbn13())
                .test()
                .assertComplete()
                .assertValue(true);
    }

    @Test public void testClear() throws Exception {
        bookContentProviderCache
                .clear()
                .test()
                .assertComplete();

        Cursor retrievedBooks = this.context.getContentResolver()
                .query(
                        VilibraContract.BookEntry.CONTENT_URI,
                        new String[]{VilibraContract.BookEntry.COLUMN_BOOK_ID},
                        null, null, null);

        boolean areThereAnyBooksInTheCache = retrievedBooks.moveToFirst();
        assertThat(areThereAnyBooksInTheCache, is(false));
    }
}
