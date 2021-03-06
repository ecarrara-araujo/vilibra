package br.eng.ecarrara.vilibra.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.data.VilibraContract.LendingEntry;

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
        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                BookEntry.COLUMN_BOOK_ID + " INTEGER PRIMARY KEY, " +
                BookEntry.COLUMN_ISBN_10 + " TEXT NOT NULL, " +
                BookEntry.COLUMN_ISBN_13 + " TEXT NOT NULL, " +
                BookEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                BookEntry.COLUMN_SUBTITLE + " TEXT, " +
                BookEntry.COLUMN_AUTHORS + " TEXT NOT NULL, " +
                BookEntry.COLUMN_PUBLISHER + " TEXT, " +
                BookEntry.COLUMN_PUBLISHED_DATE + " TEXT, " +
                BookEntry.COLUMN_PAGES + " INTEGER " +
                ");";

        final String SQL_CREATE_LENDING_TABLE = "CREATE TABLE " + LendingEntry.TABLE_NAME + " (" +
                LendingEntry.COLUMN_LENDING_ID + " INTEGER PRIMARY KEY, " +
                LendingEntry.COLUMN_CONTACT_URI + " TEXT NOT NULL, " +
                LendingEntry.COLUMN_LENDING_DATE + " TEXT NOT NULL, " +
                LendingEntry.COLUMN_LAST_NOTIFICATION_DATE + " TEXT NOT NULL, " +
                LendingEntry.COLUMN_BOOK_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + LendingEntry.COLUMN_BOOK_ID +
                ") REFERENCES " + BookEntry.TABLE_NAME + " ( " +
                    BookEntry.COLUMN_BOOK_ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_LENDING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
}
