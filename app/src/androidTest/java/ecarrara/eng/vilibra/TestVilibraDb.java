package ecarrara.eng.vilibra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.data.VilibraDbHelper;
import ecarrara.eng.vilibra.testutils.TestDataHelper;
import ecarrara.eng.vilibra.testutils.ValidationHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class TestVilibraDb {

    private static final String LOG_TAG = TestVilibraDb.class.getSimpleName();

    private Context context;
    private VilibraDbHelper vilibraSQLiteDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private long testBookId = -1L;

    @Before public void setUp() {
        this.context = InstrumentationRegistry.getTargetContext();
        boolean databaseDeleted = this.context.deleteDatabase(VilibraDbHelper.DATABASE_NAME);
        this.vilibraSQLiteDatabaseHelper = new VilibraDbHelper(this.context);
        this.sqLiteDatabase = this.vilibraSQLiteDatabaseHelper.getWritableDatabase();
    }

    @After public void cleanUp() {
        this.sqLiteDatabase.close();
        this.context.deleteDatabase(VilibraDbHelper.DATABASE_NAME);
    }

    @Test public void testSuccessfulDatabaseCreation() {
        assertThat(this.sqLiteDatabase.isOpen(), is(true));
    }

    @Test public void testDatabaseStructureForSuccessfulBookEntryInsertion() {
        assertThat(this.sqLiteDatabase.isOpen(), is(true));

        // testing book entry insertion
        ContentValues testBookValues = TestDataHelper.createAndroidRecipesValues();
        this.testBookId = this.sqLiteDatabase.insert(BookEntry.TABLE_NAME, null, testBookValues);
        assertThat(this.testBookId, is(not(-1L))); // Check if the row was really inserted
        Log.d(LOG_TAG, "New book id: " + this.testBookId);

        // Testing book insertion
        Cursor cursor = this.sqLiteDatabase.query(BookEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, testBookValues);
        cursor.close();
    }

    @Test public void testDatabaseStructureViaSimpleInsertion() {
        testDatabaseStructureForSuccessfulBookEntryInsertion();
        assertThat(this.sqLiteDatabase.isOpen(), is(true));

        // Testing lending entry insertion
        ContentValues testLendingValues = TestDataHelper.createLendingEntry(this.testBookId);
        long lendingId = this.sqLiteDatabase.insert(LendingEntry.TABLE_NAME, null, testLendingValues);
        assertThat(lendingId, is(not(-1L))); // Check if the row was really inserted
        Log.d(LOG_TAG, "New lending id: " + lendingId);

        Cursor cursor = this.sqLiteDatabase.query(LendingEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ValidationHelper.validateCursor(cursor, testLendingValues);
        cursor.close();
    }

}
