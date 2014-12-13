package ecarrara.eng.vilibra.testutils;

import android.content.ContentValues;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
/**
 * Created by ecarrara on 11/12/2014.
 */
public class TestDataHelper {

    public static final String TEST_ISBN_10 = "1430234148";

    public static ContentValues createAndroidRecipesValues() {
        ContentValues testValues = new ContentValues();

        testValues.put(BookEntry.COLUMN_ISBN_10, TEST_ISBN_10);
        testValues.put(BookEntry.COLUMN_ISBN_13, "9781430234142");
        testValues.put(BookEntry.COLUMN_TITLE, "Android Recipes");
        testValues.put(BookEntry.COLUMN_SUBTITLE, "A Problem-Solution Approach");
        testValues.put(BookEntry.COLUMN_AUTHORS, "Jeff Friesen; Dave Smith");
        testValues.put(BookEntry.COLUMN_PUBLISHER, "Apress");
        testValues.put(BookEntry.COLUMN_PUBLISHED_DATE, "20110501");
        testValues.put(BookEntry.COLUMN_PAGES, 456);

        return testValues;
    }

    public static ContentValues createLendingEntry(long bookId) {
        ContentValues testValues = new ContentValues();

        testValues.put(LendingEntry.COLUMN_BOOK_KEY, bookId);
        testValues.put(LendingEntry.COLUMN_CONTACT_URI, "fake contact for test");
        testValues.put(LendingEntry.COLUMN_LENDING_DATE, "20141213");

        return testValues;
    }

}
