package ecarrara.eng.vilibra;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.data.VilibraDbHelper;
import ecarrara.eng.vilibra.testutils.TestDataHelper;
import ecarrara.eng.vilibra.testutils.ValidationHelper;

/**
 * Created by ecarrara on 13/12/2014.
 */
public class TestVilibraDb extends AndroidTestCase {

    private static final String LOG_TAG = TestVilibraDb.class.getSimpleName();

    public void testCreateDb() {
        mContext.deleteDatabase(VilibraDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new VilibraDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertReadDb() {

        VilibraDbHelper dbHelper = new VilibraDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testBookValues = TestDataHelper.createAndroidRecipesValues();

        long bookRowId = db.insert(BookEntry.TABLE_NAME, null, testBookValues);
        assertTrue(bookRowId != -1); // Check if the row was really inserted
        Log.d(LOG_TAG, "New book id: " + bookRowId);

        // Testing book insertion
        Cursor cursor = db.query(BookEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, testBookValues);

        // Testing lending entry insertion
        ContentValues testLendingValues = TestDataHelper.createLendingEntry(bookRowId);
        long lendingRowID = db.insert(LendingEntry.TABLE_NAME, null, testLendingValues);
        assertTrue(lendingRowID != -1); // Check if the row was really inserted
        Log.d(LOG_TAG, "New lending id: " + lendingRowID);

        cursor = db.query(LendingEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, testLendingValues);

        dbHelper.close();
    }

}
