package br.eng.ecarrara.vilibra.core.data.datasource.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VilibraDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION  = 1;
    public static final String DATABASE_NAME = "vilibra.db";

    public VilibraDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // After some research I figured out that the institutions that distributes ISBNs numbers
        // sometimes make mistakes distributing the same ISBN number for different books, so
        // our database should allow that.
        // Also we keep a local id, just in case new types of identifiers are added, that will
        // make any changes in our db easier.
        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + VilibraContract.BookEntry.TABLE_NAME + " (" +
                VilibraContract.BookEntry.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY, " +
                VilibraContract.BookEntry.COLUMN_ISBN_10 + " TEXT NOT NULL, " +
                VilibraContract.BookEntry.COLUMN_ISBN_13 + " TEXT NOT NULL, " +
                VilibraContract.BookEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                VilibraContract.BookEntry.COLUMN_SUBTITLE + " TEXT, " +
                VilibraContract.BookEntry.COLUMN_AUTHORS + " TEXT NOT NULL, " +
                VilibraContract.BookEntry.COLUMN_PUBLISHER + " TEXT, " +
                VilibraContract.BookEntry.COLUMN_PUBLISHED_DATE + " TEXT, " +
                VilibraContract.BookEntry.COLUMN_PAGES + " INTEGER " +
                ");";

        final String SQL_CREATE_LENDING_TABLE = "CREATE TABLE " + VilibraContract.LendingEntry.TABLE_NAME + " (" +
                VilibraContract.LendingEntry.COLUMN_LENDING_ID + " INTEGER PRIMARY KEY, " +
                VilibraContract.LendingEntry.COLUMN_CONTACT_URI + " TEXT NOT NULL, " +
                VilibraContract.LendingEntry.COLUMN_LENDING_DATE + " TEXT NOT NULL, " +
                VilibraContract.LendingEntry.COLUMN_LAST_NOTIFICATION_DATE + " TEXT NOT NULL, " +
                VilibraContract.LendingEntry.COLUMN_BOOK_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + VilibraContract.LendingEntry.COLUMN_BOOK_ID +
                ") REFERENCES " + VilibraContract.BookEntry.TABLE_NAME + " ( " +
                    VilibraContract.BookEntry.COLUMN_BOOK_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_LENDING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
}
