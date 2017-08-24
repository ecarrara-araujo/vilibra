package br.eng.ecarrara.vilibra.android.presentation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.BookListActivity;
import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.testutils.TestDataHelper;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class TestLoanedBooksListWithContent {

    private String bookTitle;
    private String bookAuthors;

    private ContentValues testBookValues;
    private ContentValues testLendingValues;

    @Rule public ActivityTestRule<BookListActivity> activityRule =
            new ActivityTestRule<>(BookListActivity.class);


    @Before public void prepareData() {
        prepareTestData(getTargetContext());
    }

    @After public void clearData() {
        clearTestData(getTargetContext());
    }

    @Test public void testLoanedBooksListWithOneLoanedBook() {
        onView(allOf(withId(R.id.book_name_text_view), hasSibling(withText(this.bookTitle))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.book_author_text_view), hasSibling(withText(this.bookAuthors))))
                .check(matches(isDisplayed()));
    }

    private void prepareTestData(Context context) {
        clearTestData(context);

        testBookValues = TestDataHelper.createAndroidRecipesValues();

        // add a book
        Uri bookUri = context.getContentResolver()
                .insert(VilibraContract.BookEntry.CONTENT_URI, testBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertThat(bookRowId, not(-1L));


        // add a lending
        testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = context.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, testLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertThat(lendingInsertUri, notNullValue());

        bookTitle = testBookValues.getAsString(VilibraContract.BookEntry.COLUMN_TITLE);
        bookAuthors = testBookValues.getAsString(VilibraContract.BookEntry.COLUMN_AUTHORS).replace(";", ",");
    }

    private void clearTestData(Context context) {
        context.getContentResolver().delete(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null
        );
        context.getContentResolver().delete(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null
        );
    }

}
