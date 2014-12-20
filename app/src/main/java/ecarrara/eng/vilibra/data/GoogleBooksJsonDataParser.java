package ecarrara.eng.vilibra.data;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;

/**
 * Created by ecarrara on 11/12/2014.
 */
public class GoogleBooksJsonDataParser {

    private static final String LOG_TAG = GoogleBooksJsonDataParser.class.getSimpleName();

    // These are the names of the JSON objects that need to be extracted.
    // Book List
    private final String BOOKS_LIST = "items";

    // Book Volume Information
    private final String BOOK_VOLUME = "volumeInfo";

    private final String BOOK_TITLE = "title";
    private final String BOOK_SUBTITLE = "subtitle";
    private final String BOOK_AUTHORS_ARRAY = "authors";
    private final String BOOK_PUBLISHER = "publisher";
    private final String BOOK_PUBLISHED_DATE = "publishedDate";
    private final String BOOK_PAGE_COUNT = "pageCount";

    // Industry Identifiers
    private final String BOOK_INDUSTRY_IDENTIFIERS_ARRAY = "industryIdentifiers";
    private final String BOOK_ISBN_TYPE = "type";
    private final String BOOK_ISBN_IDENTIFIER = "identifier";
    private final String BOOK_ISBN_TYPE_10 = "ISBN_10";
    private final String BOOK_ISBN_TYPE_13 = "ISBN_13";

    public ContentValues parse(String testBookJson) {

        ContentValues bookEntryValues = null;

        JSONObject bookListJson = null;

        try {
            bookListJson = new JSONObject(testBookJson);
            JSONArray booksArray = bookListJson.getJSONArray(BOOKS_LIST);

            if(booksArray.length() < 1) {
                Log.i(LOG_TAG, "No books in the book list to be parsed, book not found.");
                return null;
            } else if(booksArray.length() > 1) {
                Log.i(LOG_TAG, "More than one occurence found, only the first one will be returned.");
            }

            // If necessary to process the whole list I can improve this loop later
            for (int i = 0; i < 1; i++) {
                String title = "";
                String subtitle = "";
                String authors = "";
                String publisher = "";
                String  publishedDate = "";
                int pageCount = 0;
                String isbn10 = "";
                String isbn13 = "";

                JSONObject bookInformation = booksArray.getJSONObject(i);
                JSONObject volumeInfo = bookInformation.getJSONObject(BOOK_VOLUME);
                title = volumeInfo.getString(BOOK_TITLE);
                subtitle = volumeInfo.getString(BOOK_SUBTITLE);

                JSONArray authorsJsonArray = volumeInfo.getJSONArray(BOOK_AUTHORS_ARRAY);
                for (int j = 0; j < authorsJsonArray.length(); j++) {
                    String author = authorsJsonArray.getString(j);
                    if(j == authorsJsonArray.length() - 1) {
                        authors += author;
                    } else {
                        authors += author + VilibraContract.AUTHORS_SEPARATOR + " ";
                    }
                }

                publisher = volumeInfo.getString(BOOK_PUBLISHER);
                publishedDate = cleanUpDateFromJson(volumeInfo.getString(BOOK_PUBLISHED_DATE));

                JSONArray industryIdetJsonArray =
                        volumeInfo.getJSONArray(BOOK_INDUSTRY_IDENTIFIERS_ARRAY);
                for (int j = 0; j < industryIdetJsonArray.length(); j++) {
                    JSONObject identifier = industryIdetJsonArray.getJSONObject(j);
                    String type = identifier.getString(BOOK_ISBN_TYPE);
                    if(BOOK_ISBN_TYPE_10.equals(type)) {
                        isbn10 = identifier.getString(BOOK_ISBN_IDENTIFIER);
                    } else if (BOOK_ISBN_TYPE_13.equals(type)) {
                        isbn13 = identifier.getString(BOOK_ISBN_IDENTIFIER);
                    }
                }

                pageCount = volumeInfo.getInt(BOOK_PAGE_COUNT);
                bookEntryValues = createContentValuesForBookEntry(title, subtitle, authors, publisher,
                        publishedDate, isbn10, isbn13, pageCount);
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, "Error parsing book json data: " + e.getMessage() );
            e.printStackTrace();
        }
        return bookEntryValues;
    }

    private String cleanUpDateFromJson(String jsonDate) {
        final String jsonDateSep = "-";
        return jsonDate.replace(jsonDateSep, "");
    }

    private ContentValues createContentValuesForBookEntry(String title, String subtitle,
                                                          String authors, String publisher,
                                                          String publishedDate, String isbn10,
                                                          String isbn13, int pageCount) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BookEntry.COLUMN_ISBN_10, isbn10);
        contentValues.put(BookEntry.COLUMN_ISBN_13, isbn13);
        contentValues.put(BookEntry.COLUMN_TITLE, title);
        contentValues.put(BookEntry.COLUMN_SUBTITLE, subtitle);
        contentValues.put(BookEntry.COLUMN_AUTHORS, authors);
        contentValues.put(BookEntry.COLUMN_PUBLISHER, publisher);
        contentValues.put(BookEntry.COLUMN_PUBLISHED_DATE, publishedDate);
        contentValues.put(BookEntry.COLUMN_PAGES, pageCount);

        return contentValues;
    }
}
