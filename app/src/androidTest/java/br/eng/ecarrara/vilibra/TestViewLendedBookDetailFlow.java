package br.eng.ecarrara.vilibra;


import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract;
import br.eng.ecarrara.vilibra.testutils.TestDataHelper;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Test the visualization of the details of the selected item.
 */
@RunWith(AndroidJUnit4.class)
public class TestViewLendedBookDetailFlow {

    private String mBookISBN10;
    private String mBookISBN13;
    private String mBookTitle;
    private String mBookSubtitle;
    private String mBookAuthors;
    private String mBookPublisher;
    private String mBookPages;

    private ContentValues mTestBookValues;
    private ContentValues mTestLendingValues;

    private Context mContext;

    @Rule
    public ActivityTestRule<BookListActivity> mActivityRule =
            new ActivityTestRule<>(BookListActivity.class);

    @Before public void setUp() throws Exception {
        mContext = getTargetContext();
        prepareTestData();
        Activity activity = mActivityRule.getActivity();
    }

    @After public void tearDown() throws Exception {
        clearTestData();
    }

    @Test public void testViewBorrowedBookDetail() {

        onView(allOf(withText(this.mBookTitle))).perform(click());

        // check data in the detail screen
        onView(ViewMatchers.withId(R.id.book_title_text_view))
                .check(matches(withText(mBookTitle)));
        onView(withId(R.id.book_subtitle_text_view))
                .check(matches(withText(mBookSubtitle)));
        onView(withId(R.id.book_authors_text_view))
                .check(matches(withText(mBookAuthors)));
        onView(withId(R.id.book_publisher_edition_text_view))
                .check(matches(withText(mBookPublisher)));

        pressBack();

    }

    private void prepareTestData() {
        clearTestData();

        mTestBookValues = TestDataHelper.createAndroidRecipesValues();

        // add a book
        Uri bookUri = mContext.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, mTestBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertThat(bookRowId, not(-1L));

        // add a lending
        mTestLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = mContext.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, mTestLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertThat(lendingInsertUri, notNullValue());

        mBookISBN10 = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_ISBN_10);
        mBookISBN13 = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_ISBN_13);
        mBookTitle = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_TITLE);
        mBookSubtitle = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_SUBTITLE);
        mBookAuthors = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_AUTHORS);
        mBookPublisher = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_PUBLISHER);
        mBookPages = mTestBookValues.getAsString(VilibraContract.BookEntry.COLUMN_PAGES);
    }

    private void clearTestData() {
        mContext.getContentResolver().delete(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null
        );
    }
}
