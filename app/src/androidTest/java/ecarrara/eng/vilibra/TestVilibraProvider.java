package ecarrara.eng.vilibra;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.testutils.TestDataHelper;
import ecarrara.eng.vilibra.testutils.ValidationHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TestVilibraProvider {
    private static final String LOG_TAG = TestVilibraProvider.class.getSimpleName();
    private static final long INVALID_ROW_ID = -1L;

    private Context context;
    private long testBookId = INVALID_ROW_ID;
    private long testLendingId = INVALID_ROW_ID;
    private ContentValues testBookContentValues = null;
    private ContentValues testLendingContentValues = null;

    @Before public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
        deleteAllRecords();

        this.testBookContentValues = TestDataHelper.createAndroidRecipesValues();
    }

    @After public void cleanUp() {
        deleteAllRecords();
    }

    @Test public void testSuccessfulInsertBookEntry() {
        Uri bookUri = this.context.getContentResolver()
                .insert(BookEntry.CONTENT_URI, this.testBookContentValues);
        this.testBookId = ContentUris.parseId(bookUri);
        assertThat(this.testBookId, is(not(INVALID_ROW_ID)));

        Cursor cursor = this.context.getContentResolver().query(
                BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, this.testBookContentValues);

        cursor = this.context.getContentResolver().query(
                BookEntry.buildBookUri(this.testBookId),
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, this.testBookContentValues);
    }

    @Test public void testSuccessfulInsertLendingEntry() {
        testSuccessfulInsertBookEntry();

        // Trying to add a lending
        this.testLendingContentValues = TestDataHelper.createLendingEntry(this.testBookId);
        Uri lendingInsertUri = this.context.getContentResolver()
                .insert(LendingEntry.CONTENT_URI, this.testLendingContentValues);
        this.testLendingId = ContentUris.parseId(lendingInsertUri);
        assertThat(this.testLendingId, is(not(INVALID_ROW_ID)));

        Cursor lendingCursor = this.context.getContentResolver().query(
                LendingEntry.CONTENT_URI,  // Table to Query
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(lendingCursor, this.testLendingContentValues);
    }

    @Test public void testSuccessfulBookLendingJoinQuery() {
        testSuccessfulInsertLendingEntry();

        // Add the book values in with the lending data so that we can make
        // sure that the join worked and we actually get all the values back
        addAllContentValues(this.testLendingContentValues, this.testBookContentValues);

        // Get the specific joined Book and Lending Info
        Cursor cursor = this.context.getContentResolver().query(
                LendingEntry.buildLendingWithBookUri(this.testLendingId, this.testBookId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(cursor, this.testLendingContentValues);

        // Get the all joined Book and Lending info
        cursor = this.context.getContentResolver().query(
                LendingEntry.buildLendingBooksUri(),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(cursor, this.testLendingContentValues);
    }

    @Test public void testGetBookEntryType() {
        String type = this.context.getContentResolver().getType(BookEntry.CONTENT_URI);
        assertThat(type, is(BookEntry.CONTENT_TYPE));

        type = this.context.getContentResolver().getType(BookEntry.buildBookUri(1L));
        assertThat(type, is(BookEntry.CONTENT_ITEM_TYPE));
    }

    @Test public void testGetLendingEntryType() {
        String type = this.context.getContentResolver().getType(LendingEntry.CONTENT_URI);
        assertThat(type, is(LendingEntry.CONTENT_TYPE));

        type = this.context.getContentResolver().getType(LendingEntry.buildLendingUri(1L));
        assertThat(type, is(LendingEntry.CONTENT_ITEM_TYPE));

        type = this.context.getContentResolver().getType(LendingEntry.buildLendingWithBookUri(1L, 1L));
        assertThat(type, is(LendingEntry.CONTENT_ITEM_TYPE));

        type = this.context.getContentResolver().getType(LendingEntry.buildLendingBooksUri());
        assertThat(type, is(LendingEntry.CONTENT_TYPE));
    }

    @Test public void testSuccessfulBookUpdate() {
        testSuccessfulInsertBookEntry();

        ContentValues updatedValues = new ContentValues(this.testBookContentValues);
        updatedValues.put(BookEntry.COLUMN_BOOK_ID, this.testBookId);
        updatedValues.put(BookEntry.COLUMN_TITLE, "Ain't nobody Got Time for Dat");

        int count = this.context.getContentResolver().update(
                BookEntry.CONTENT_URI, updatedValues, BookEntry.COLUMN_BOOK_ID + "= ?",
                new String[] { Long.toString(this.testBookId)});

        assertThat(count, is(1)); // only one registry must be updated

        Cursor cursor = this.context.getContentResolver().query(
                BookEntry.buildBookUri(this.testBookId),
                null,
                null,
                null,
                null
        );

        ValidationHelper.validateCursor(cursor, updatedValues);
    }

    @Test public void testSuccessfulLendingUpdate() {
        testSuccessfulInsertLendingEntry();

        ContentValues updatedValues = new ContentValues(this.testLendingContentValues);
        updatedValues.put(LendingEntry.COLUMN_LENDING_ID, this.testLendingId);
        updatedValues.put(LendingEntry.COLUMN_LENDING_DATE, "The day of the end of the World");

        int count = this.context.getContentResolver().update(
                LendingEntry.CONTENT_URI, updatedValues, LendingEntry.COLUMN_LENDING_ID + "= ?",
                new String[] { Long.toString(this.testLendingId)});

        assertThat(count, is(1)); // only one registry must be updated

        Cursor cursor = this.context.getContentResolver().query(
                LendingEntry.buildLendingUri(this.testLendingId),
                null,
                null,
                null,
                null
        );

        ValidationHelper.validateCursor(cursor, updatedValues);
    }

    // Make sure we can still delete after adding/updating stuff
    @Test public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }


    // The target api annotation is needed for the call to keySet -- we wouldn't want
    // to use this in our app, but in a test it's fine to assume a higher target.
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet()) {
            destination.put(key, source.getAsString(key));
        }
    }

    // brings our database to an empty state
    private void deleteAllRecords() {
        this.context.getContentResolver().delete(
                LendingEntry.CONTENT_URI,
                null,
                null
        );
        this.context.getContentResolver().delete(
                BookEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = this.context.getContentResolver().query(
                BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(cursor, is(not(nullValue())));
        assertThat(cursor.getCount(), is(0));
        cursor.close();

        cursor = this.context.getContentResolver().query(
                LendingEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(cursor, is(not(nullValue())));
        assertThat(cursor.getCount(), is(0));
        cursor.close();
    }

}
