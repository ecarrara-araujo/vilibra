package ecarrara.eng.vilibra;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.util.Log;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.testutils.TestDataHelper;
import ecarrara.eng.vilibra.testutils.ValidationHelper;

/**
 * Created by ecarrara on 13/12/2014.
 */
public class TestVilibraProvider extends AndroidTestCase {
    private static final String LOG_TAG = TestVilibraProvider.class.getSimpleName();

    // brings our database to an empty state
    public void deleteAllRecords() {
        mContext.getContentResolver().delete(
                LendingEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                BookEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                LendingEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    public void setUp() {
        deleteAllRecords();
    }

    public void testInsertReadProvider() {

        ContentValues testBookValues = TestDataHelper.createAndroidRecipesValues();

        Uri bookUri = mContext.getContentResolver().insert(BookEntry.CONTENT_URI, testBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertTrue(bookRowId != -1);

        // Check the way back using the provider.
        Cursor cursor = mContext.getContentResolver().query(
                BookEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(cursor, testBookValues);

        // Now see if we can successfully query if we include the row id
        cursor = mContext.getContentResolver().query(
                BookEntry.buildBookUri(bookRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(cursor, testBookValues);

        // Trying to add a lending
        ContentValues testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = mContext.getContentResolver()
                .insert(LendingEntry.CONTENT_URI, testLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertTrue(lendingInsertUri != null);

        // A cursor is your primary interface to the query results.
        Cursor lendingCursor = mContext.getContentResolver().query(
                LendingEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );
        ValidationHelper.validateCursor(lendingCursor, testLendingValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        addAllContentValues(testLendingValues, testBookValues);

        // Get the specific joined Book and Lending Info
        lendingCursor = mContext.getContentResolver().query(
                LendingEntry.buildLendingWithBookUri(lendingRowId, bookRowId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(lendingCursor, testLendingValues);

        // Get the all joined Book and Lending info
        lendingCursor = mContext.getContentResolver().query(
                LendingEntry.buildLendingBooksUri(),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        ValidationHelper.validateCursor(lendingCursor, testLendingValues);
    }

    public void testGetType() {
        String type = mContext.getContentResolver().getType(BookEntry.CONTENT_URI);
        assertEquals(BookEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(BookEntry.buildBookUri(1L));
        assertEquals(BookEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LendingEntry.CONTENT_URI);
        assertEquals(LendingEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver().getType(LendingEntry.buildLendingUri(1L));
        assertEquals(LendingEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LendingEntry.buildLendingWithBookUri(1L, 1L));
        assertEquals(LendingEntry.CONTENT_ITEM_TYPE, type);

        type = mContext.getContentResolver().getType(LendingEntry.buildLendingBooksUri());
        assertEquals(LendingEntry.CONTENT_TYPE, type);
    }

    public void testUpdateBook() {
        ContentValues values = TestDataHelper.createTestBookValues();

        Uri bookUri = mContext.getContentResolver().
                insert(BookEntry.CONTENT_URI, values);
        long bookRowId = ContentUris.parseId(bookUri);

        // Verify we got a row back.
        assertTrue(bookRowId != -1);
        Log.d(LOG_TAG, "New row id: " + bookRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(BookEntry._ID, bookRowId);
        updatedValues.put(BookEntry.COLUMN_TITLE, "Ain't noboady Got Time for Dat");

        int count = mContext.getContentResolver().update(
                BookEntry.CONTENT_URI, updatedValues, BookEntry._ID + "= ?",
                new String[] { Long.toString(bookRowId)});

        assertEquals(count, 1);

        Cursor cursor = mContext.getContentResolver().query(
                BookEntry.buildBookUri(bookRowId),
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        ValidationHelper.validateCursor(cursor, updatedValues);
    }

    public void testUpdateLending() {
        ContentValues testBookValues = TestDataHelper.createTestBookValues();

        Uri bookUri = mContext.getContentResolver().
                insert(BookEntry.CONTENT_URI, testBookValues);
        long bookIdForTest = ContentUris.parseId(bookUri);

        ContentValues values = TestDataHelper.createLendingEntry(bookIdForTest);

        Uri lendingUri = mContext.getContentResolver().
                insert(LendingEntry.CONTENT_URI, values);
        long lendingRowId = ContentUris.parseId(lendingUri);

        // Verify we got a row back.
        assertTrue(lendingRowId != -1);
        Log.d(LOG_TAG, "New row id: " + lendingRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(LendingEntry._ID, lendingRowId);
        updatedValues.put(LendingEntry.COLUMN_LENDING_DATE, "The day of the end of the World");

        int count = mContext.getContentResolver().update(
                LendingEntry.CONTENT_URI, updatedValues, LendingEntry._ID + "= ?",
                new String[] { Long.toString(lendingRowId)});

        assertEquals(count, 1);

        Cursor cursor = mContext.getContentResolver().query(
                LendingEntry.buildLendingUri(lendingRowId),
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        ValidationHelper.validateCursor(cursor, updatedValues);
    }

    // Make sure we can still delete after adding/updating stuff
    public void testDeleteRecordsAtEnd() {
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

}
