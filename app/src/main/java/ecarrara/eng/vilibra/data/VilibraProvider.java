package ecarrara.eng.vilibra.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;

/**
 * Created by ecarrara on 13/12/2014.
 */
public class VilibraProvider extends ContentProvider {
    private static final String LOG_TAG = VilibraProvider.class.getSimpleName();

    private VilibraDbHelper mOpenHelper;

    // Uri match ids
    private static final int BOOK = 100; // all books
    private static final int BOOK_ID = 101; // specific book by _ID
    private static final int LENDING = 200; // all lending
    private static final int LENDING_ID = 201; // all lending
    private static final int LENDING_WITH_BOOK = 202; // lending for a specific book
    private static final int LENDING_BOOKS = 203; // all lending data joined with respective book data
    private static final int LENDING_FOR_A_BOOK = 204; // search for lending only having the book id

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = VilibraContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, VilibraContract.PATH_BOOK, BOOK);
        uriMatcher.addURI(authority, VilibraContract.PATH_BOOK + "/#", BOOK_ID);

        uriMatcher.addURI(authority, VilibraContract.PATH_LENDING, LENDING);
        uriMatcher.addURI(authority, VilibraContract.PATH_LENDING + "/#", LENDING_ID);
        uriMatcher.addURI(authority, VilibraContract.PATH_LENDING + "/#/#", LENDING_WITH_BOOK);
        uriMatcher.addURI(authority, VilibraContract.PATH_LENDING + "/"
                        + VilibraContract.PATH_BOOK, LENDING_BOOKS);
        uriMatcher.addURI(authority, VilibraContract.PATH_LENDING + "/"
                        + VilibraContract.PATH_BOOK + "/#", LENDING_FOR_A_BOOK);

        return uriMatcher;
    }

    private static final SQLiteQueryBuilder sBookWithLendingInfoQueryBuilder;
    static {
        sBookWithLendingInfoQueryBuilder = new SQLiteQueryBuilder();
        sBookWithLendingInfoQueryBuilder.setTables(
                LendingEntry.TABLE_NAME + " INNER JOIN " + BookEntry.TABLE_NAME +
                        " ON " + LendingEntry.TABLE_NAME + "." + LendingEntry.COLUMN_BOOK_KEY +
                        " = " + BookEntry.TABLE_NAME + "." + BookEntry._ID
        );
    }

    private static final String sBookIdAndLendingIdSelection =
            BookEntry.TABLE_NAME + "." + BookEntry._ID + " = ? AND " +
            LendingEntry.TABLE_NAME + "." + LendingEntry._ID + " = ? ";

    private Cursor getLendingInfoByBookAndLending(Uri uri, String[] projection, String sortOrder) {
        String lendingId = LendingEntry.getLendingIdFromUri(uri);
        String bookId = LendingEntry.getBookIdFromUri(uri);

        String[] selectionArgs = new String[]{ bookId, lendingId };
        String selection = sBookIdAndLendingIdSelection;

        return sBookWithLendingInfoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
    }

    private static final String sBookIdSelection =
            BookEntry.TABLE_NAME + "." + BookEntry._ID + " = ? ";

    private Cursor getLendingInfoByBook(Uri uri, String[] projection, String sortOrder) {
        String bookId = LendingEntry.getBookIdFromUri(uri);

        String[] selectionArgs = new String[]{ bookId };
        String selection = sBookIdSelection;

        return sBookWithLendingInfoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new VilibraDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)) {
            case BOOK:
                retCursor = mOpenHelper.getReadableDatabase().query(BookEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(BookEntry.TABLE_NAME,
                        projection,
                        BookEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null, null, null, sortOrder);
                break;
            case LENDING:
                retCursor = mOpenHelper.getReadableDatabase().query(LendingEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LENDING_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(LendingEntry.TABLE_NAME,
                        projection,
                        LendingEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null, null, null, sortOrder);
                break;
            case LENDING_WITH_BOOK:
                retCursor = getLendingInfoByBookAndLending(uri, projection, sortOrder);
                break;
            case LENDING_BOOKS:
                retCursor = sBookWithLendingInfoQueryBuilder.query(mOpenHelper.getWritableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LENDING_FOR_A_BOOK:
                retCursor = getLendingInfoByBook(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case BOOK:
                return BookEntry.CONTENT_TYPE;
            case BOOK_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            case LENDING:
                return LendingEntry.CONTENT_TYPE;
            case LENDING_ID:
                return LendingEntry.CONTENT_ITEM_TYPE;
            case LENDING_WITH_BOOK:
                return LendingEntry.CONTENT_ITEM_TYPE;
            case LENDING_BOOKS:
                return LendingEntry.CONTENT_TYPE;
            case LENDING_FOR_A_BOOK:
                return LendingEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case BOOK: {
                long _id = db.insert(BookEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = BookEntry.buildBookUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LENDING: {
                long _id = db.insert(LendingEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = LendingEntry.buildLendingUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case BOOK:
                rowsDeleted = db.delete(
                        BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LENDING:
                rowsDeleted = db.delete(
                        LendingEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LENDING_ID:
                String lendingId = Long.toString(ContentUris.parseId(uri));
                rowsDeleted = db.delete(
                        LendingEntry.TABLE_NAME, LendingEntry._ID + " = ?",
                        new String[] { lendingId });
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case BOOK:
                rowsUpdated = db.update(BookEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LENDING:
                rowsUpdated = db.update(LendingEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
