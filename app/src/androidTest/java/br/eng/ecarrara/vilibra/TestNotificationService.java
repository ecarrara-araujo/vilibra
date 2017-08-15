package br.eng.ecarrara.vilibra;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.test.ServiceTestCase;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.notification.BookLendingNotificationService;
import br.eng.ecarrara.vilibra.testutils.TestDataHelper;

/**
 * Created by ecarrara on 30/12/2014.
 */
public class TestNotificationService extends ServiceTestCase<BookLendingNotificationService> {
    private static final String LOG_TAG = TestNotificationService.class.getSimpleName();

    public TestNotificationService() {
        super(BookLendingNotificationService.class);
    }


    public void setUp() {
        deleteAllRecords();
    }
    public void deleteAllRecords() {
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

        Cursor cursor = mContext.getContentResolver().query(
                VilibraContract.BookEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                VilibraContract.LendingEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    public void testSingleNotification() {

        // Add Book data
        ContentValues testBookValues = TestDataHelper.createAndroidRecipesValues();
        Uri bookUri = mContext.getContentResolver().insert(VilibraContract.BookEntry.CONTENT_URI, testBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertTrue(bookRowId != -1);

        // Add a lending
        ContentValues testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = mContext.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, testLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertTrue(lendingInsertUri != null);

        Intent intent = new Intent(getContext(), BookLendingNotificationService.class);
        startService(intent);

        Log.d(LOG_TAG, "No notifications should be generated.");

        testLendingValues.put(VilibraContract.LendingEntry.COLUMN_LAST_NOTIFICATION_DATE,
                getFutureDateString(testLendingValues.getAsString(VilibraContract.LendingEntry.COLUMN_LENDING_DATE)));
        mContext.getContentResolver().update(lendingInsertUri, testLendingValues, null, null);

        intent = new Intent(getContext(), BookLendingNotificationService.class);
        startService(intent);

        Log.d(LOG_TAG, "One notification should be generated.");
    }

    public void testMultipleNotifications() {

        // Add Book data
        ContentValues testBookValues = TestDataHelper.createAndroidRecipesValues();
        Uri bookUri = mContext.getContentResolver().insert(VilibraContract.BookEntry.CONTENT_URI, testBookValues);
        long bookRowId = ContentUris.parseId(bookUri);
        assertTrue(bookRowId != -1);

        // Add a lending
        ContentValues testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        Uri lendingInsertUri = mContext.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, testLendingValues);
        long lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertTrue(lendingInsertUri != null);

        testLendingValues.put(VilibraContract.LendingEntry.COLUMN_LAST_NOTIFICATION_DATE,
                getFutureDateString(testLendingValues.getAsString(VilibraContract.LendingEntry.COLUMN_LENDING_DATE)));
        mContext.getContentResolver().update(lendingInsertUri, testLendingValues, null, null);

        // Add new Book data
        testBookValues = TestDataHelper.createTestBookValues();
        bookUri = mContext.getContentResolver().insert(VilibraContract.BookEntry.CONTENT_URI, testBookValues);
        bookRowId = ContentUris.parseId(bookUri);
        assertTrue(bookRowId != -1);

        // Add new lending
        testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        lendingInsertUri = mContext.getContentResolver()
                .insert(VilibraContract.LendingEntry.CONTENT_URI, testLendingValues);
        lendingRowId = ContentUris.parseId(lendingInsertUri);
        assertTrue(lendingInsertUri != null);

        testLendingValues.put(VilibraContract.LendingEntry.COLUMN_LAST_NOTIFICATION_DATE,
                getFutureDateString(testLendingValues.getAsString(VilibraContract.LendingEntry.COLUMN_LENDING_DATE)));
        mContext.getContentResolver().update(lendingInsertUri, testLendingValues, null, null);

        Intent intent = new Intent(getContext(), BookLendingNotificationService.class);
        startService(intent);

        Log.d(LOG_TAG, "Summary notification should be generated.");
    }

    private String getFutureDateString(String currentNotificationDateString) {
        Date currentNotificationDate = VilibraContract.getDateFromDb(currentNotificationDateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentNotificationDate);
        calendar.add(Calendar.DATE, BookLendingNotificationService.LENDING_NOTIFICATION_INTERVAL + 1);

        return VilibraContract.getDbDateString(calendar.getTime());
    }

}
