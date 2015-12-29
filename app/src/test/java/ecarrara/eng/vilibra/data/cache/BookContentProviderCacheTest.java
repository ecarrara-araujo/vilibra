package ecarrara.eng.vilibra.data.cache;

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

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.domain.cache.Cache;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.test.fixture.BookFixture;
import ecarrara.eng.vilibra.test.fixture.VilibraProviderFixture;
import ecarrara.eng.vilibra.utils.RobolectricUtils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookContentProviderCacheTest {

    private Context context;
    private VilibraProviderFixture vilibraProviderFixture;

    private Book testBook;
    private Cache<String, Book> bookContentProviderCache;

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
        Book retrievedBook = this.bookContentProviderCache.get(this.testBook.getIsbn10());
        assertThat(retrievedBook, equalTo(this.testBook));
    }

    @Test public void testGetWithIsbn13() throws Exception {
        Book retrievedBook = this.bookContentProviderCache.get(this.testBook.getIsbn13());
        assertThat(retrievedBook, equalTo(this.testBook));
    }

    @Test public void testPutWithIsbn10() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.getTestBookDevsTestBook();
        this.bookContentProviderCache.put(anotherBook.getIsbn10(), anotherBook);
        Book retrievedBook = this.bookContentProviderCache.get(anotherBook.getIsbn10());
        assertThat(retrievedBook, equalTo(anotherBook));
    }

    @Test public void testPutWithIsbn13() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.getTestBookDevsTestBook();
        this.bookContentProviderCache.put(anotherBook.getIsbn13(), anotherBook);
        Book retrievedBook = this.bookContentProviderCache.get(anotherBook.getIsbn13());
        assertThat(retrievedBook, equalTo(anotherBook));
    }

    @Test public void testPutWithIsbn10AdGetWithIsbn13() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.getTestBookDevsTestBook();
        this.bookContentProviderCache.put(anotherBook.getIsbn10(), anotherBook);
        Book retrievedBook = this.bookContentProviderCache.get(anotherBook.getIsbn13());
        assertThat(retrievedBook, equalTo(anotherBook));
    }

    @Test public void testPutWithIsbn13AdGetWithIsbn10() throws Exception {
        this.vilibraProviderFixture.clearVilibraDatabase();
        Book anotherBook = BookFixture.getTestBookDevsTestBook();
        this.bookContentProviderCache.put(anotherBook.getIsbn13(), anotherBook);
        Book retrievedBook = this.bookContentProviderCache.get(anotherBook.getIsbn10());
        assertThat(retrievedBook, equalTo(anotherBook));
    }

    @Test public void testRemoveWithIsbn10() throws Exception {
        this.bookContentProviderCache.remove(this.testBook.getIsbn10());
        Book retrievedBook = this.bookContentProviderCache.get(this.testBook.getIsbn10());
        assertThat(retrievedBook, equalTo(Book.NO_BOOK));
    }

    @Test public void testRemoveWithIsbn13() throws Exception {
        this.bookContentProviderCache.remove(this.testBook.getIsbn13());
        Book retrievedBook = this.bookContentProviderCache.get(this.testBook.getIsbn10());
        assertThat(retrievedBook, equalTo(Book.NO_BOOK));
    }

    @Test public void testIsCachedWithIsbn10() throws Exception {
        boolean isBookCached = this.bookContentProviderCache.isCached(this.testBook.getIsbn10());
        assertThat(isBookCached, is(true));
    }

    @Test public void testIsCachedWithIsbn13() throws Exception {
        boolean isBookCached = this.bookContentProviderCache.isCached(this.testBook.getIsbn13());
        assertThat(isBookCached, is(true));
    }

    @Test public void testClear() throws Exception {
        this.bookContentProviderCache.clear();

        Cursor retrievedBooks = this.context.getContentResolver().query(
                VilibraContract.BookEntry.CONTENT_URI, new String[]{VilibraContract.BookEntry.COLUMN_BOOK_ID},
                null, null, null);

        boolean areThereAnyBooksInTheCache = retrievedBooks.moveToFirst();
        assertThat(areThereAnyBooksInTheCache, is(false));
    }
}
