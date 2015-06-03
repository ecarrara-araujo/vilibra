package ecarrara.eng.vilibra;


import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.CursorMatchers;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.testutils.TestDataHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ecarrara on 07/04/2015.
 */
public class TestAddLendedBookFlow extends ActivityInstrumentationTestCase2<BookListActivity> {

    private String mBookISBN10;
    private String mBookISBN13;
    private String mBookTitle;
    private String mBookSubtitle;
    private String mBookAuthors;
    private String mBookPublisher;
    private String mBookPages;

    private BookListActivity mBookListActivity;
    private ContentValues mTestBookValues;
    private ContentValues mTestLendingValues;


    public TestAddLendedBookFlow() {
        super(BookListActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mBookListActivity = getActivity();

        prepareTestData();
    }

    @After
    public void tearDown() throws  Exception {
        clearTestData();
    }

    public void testAddLendedBookFlow() {

        // push the add lended book button
        onData(CursorMatchers.withRowString(VilibraContract.BookEntry.COLUMN_TITLE, mBookTitle))
                .inAdapterView(withId(R.id.lended_book_list_view))
                .perform(click());

        // check data in the detail screen
        onView(withId(R.id.book_title_text_view)).check(matches(withText(mBookTitle)));
        onView(withId(R.id.book_subtitle_text_view)).check(matches(withText(mBookSubtitle)));
        onView(withId(R.id.book_authors_text_view)).check(matches(withText(mBookAuthors)));
        onView(withId(R.id.book_publisher_edition_text_view)).check(matches(withText(mBookPublisher)));

        //onView(withId(R.id.book_isbn10_text_view)).check(matches(withText(mBookISBN10)));

        pressBack();

    }

    private void prepareTestData() {
        mTestBookValues = TestDataHelper.createAndroidRecipesValues();

        // add a book
        Uri bookUri = mBookListActivity.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, mTestBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertTrue(bookRowId != -1);

        // add a lending
        mTestLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = mBookListActivity.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, mTestLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertTrue(lendingInsertUri != null);

        mBookISBN10 = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_ISBN_10);
        mBookISBN13 = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_ISBN_13);
        mBookTitle = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_TITLE);
        mBookSubtitle = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_SUBTITLE);
        mBookAuthors = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_AUTHORS);
        mBookPublisher = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_PUBLISHER);
        mBookPages = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_PAGES);
    }

    private void clearTestData() {
        mBookListActivity.getContentResolver().delete(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null
        );
        mBookListActivity.getContentResolver().delete(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null
        );
    }
}
