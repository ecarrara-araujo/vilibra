package ecarrara.eng.vilibra.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ecarrara on 11/12/2014.
 */
public class VilibraContract {

    public static final String CONTENT_AUTHORITY = "ecarrara.eng.vilibra";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible Paths
    public static final String PATH_BOOK = "book";
    public static final String PATH_LENDING = "lending";
    public static final String PATH_BORROWING = "borrowing";

    public static final Uri BORROWING_CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_BORROWING).build();

    // Format used for storing dates in the database.  Also used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    // Character used to separate each author in the authors field
    public static final String AUTHORS_SEPARATOR = ";";

    // Id used to specify that the entry was not persisted yet.
    public static final long ENTRY_NOT_SAVED_ID = -1L;

    /**
     * Class that defines the content table for Books information.
     */
    public static final class BookEntry {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String TABLE_NAME = "book";

        // Local book id
        public static final String COLUMN_BOOK_ID = "id";

        // The ISBN identifier format 10
        public static final String COLUMN_ISBN_10 = "isbn_10";

        // The ISBN identifier format 13
        public static final String COLUMN_ISBN_13 = "isbn_13";

        // Book main title
        public static final String COLUMN_TITLE = "title";

        // Book subtitle
        public static final String COLUMN_SUBTITLE = "subtitle";

        // List of authors separated by semi colons
        public static final String COLUMN_AUTHORS = "authors";

        // Book publisher
        public static final String COLUMN_PUBLISHER = "publisher";

        // Book published date
        public static final String COLUMN_PUBLISHED_DATE = "published_date";

        // Book page count
        public static final String COLUMN_PAGES = "page_count";

        public static Uri buildBookUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getBookIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }
    }

    /**
     * Class that defines the table contents for Book lending
     */
    public static final class LendingEntry {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LENDING).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String TABLE_NAME = "lending";

        // Local lending id
        public static final String COLUMN_LENDING_ID = "id";

        // The id of the book this lending refers to
        public static final String COLUMN_BOOK_ID = "book_id";

        // Date that the book was borrowed by this person
        public static final String COLUMN_LENDING_DATE = "lending_date";

        // Contact Uri for the person who have borrowed the book
        public static final String COLUMN_CONTACT_URI = "contact_uri";

        public static final String COLUMN_LAST_NOTIFICATION_DATE = "last_notification_date";

        public static Uri buildLendingUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildLendingWithBookUri(long lendingId, long bookId) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(lendingId))
                    .appendPath(Long.toString(bookId)).build();
        }


        public static Uri buildLendingBooksUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();
        }

        public static Uri buildLendingForABookUri(long bookId) {
            return ContentUris.withAppendedId(buildLendingBooksUri(), bookId);
        }

        public static String getLendingIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getBookIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    /**
     * Converts Date class to a string representation, used for easy comparison and database lookup.
     * @param date The input date
     * @return a DB-friendly representation of the date, using the format defined in DATE_FORMAT.
     */
    public static String getDbDateString(Date date){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Converts a dateText to a long Unix time representation
     * @param dateText the input date string
     * @return the Date object
     */
    public static Date getDateFromDb(String dateText) {
        SimpleDateFormat dbDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return dbDateFormat.parse(dateText);
        } catch ( ParseException e ) {
            e.printStackTrace();
            return null;
        }
    }

}
