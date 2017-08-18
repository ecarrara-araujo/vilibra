package br.eng.ecarrara.vilibra.android.presentation;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.eng.ecarrara.vilibra.BookListActivity;
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract;
import br.eng.ecarrara.vilibra.testutils.TestDataHelper;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class TestLoanedBooksListWithContent {

    private String bookTitle;

    private ContentValues testBookValues;
    private ContentValues testLendingValues;

    @Rule public ActivityTestRule<BookListActivity> activityRule =
            new ActivityTestRule<>(BookListActivity.class);

    @Test public void testLoanedBooksListWithOneLoanedBook() {
        prepareTestData(getTargetContext());

        onView(allOf(withText(this.bookTitle))).check(matches(isDisplayed()));

        clearTestData(getTargetContext());
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
