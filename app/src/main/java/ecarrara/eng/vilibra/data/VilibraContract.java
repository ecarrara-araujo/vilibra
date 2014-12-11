package ecarrara.eng.vilibra.data;

import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ecarrara on 11/12/2014.
 */
public class VilibraContract {

    // Format used for storing dates in the database.  Also used for converting those strings
    // back into date objects for comparison/processing.
    public static final String DATE_FORMAT = "yyyyMMdd";

    // Character used to separate each author in the authors field
    public static final String AUTHORS_SEPARATOR = ";";

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

    public static final class BookEntry implements BaseColumns {

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
    }

}
