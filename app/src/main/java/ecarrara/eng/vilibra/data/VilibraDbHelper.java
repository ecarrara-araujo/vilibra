package ecarrara.eng.vilibra.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;

/**
 * Created by ecarrara on 13/12/2014.
 */
public class VilibraDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION  = 1;
    public static final String DATABASE_NAME = "vilibra.db";

    public VilibraDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                BookEntry._ID + " INTEGER PRIMARY KEY, " +
                BookEntry.COLUMN_ISBN_10 + " TEXT UNIQUE NOT NULL, " +
                BookEntry.COLUMN_ISBN_13 + " TEXT UNIQUE NOT NULL, " +
                BookEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                BookEntry.COLUMN_SUBTITLE + " TEXT, " +
                BookEntry.COLUMN_AUTHORS + " TEXT NOT NULL, " +
                BookEntry.COLUMN_PUBLISHER + " TEXT, " +
                BookEntry.COLUMN_PUBLISHED_DATE + " TEXT, " +
                BookEntry.COLUMN_PAGES + " INTEGER, " +
                "UNIQUE (" + BookEntry.COLUMN_ISBN_13 + "," +
                BookEntry.COLUMN_ISBN_13 + ") ON CONFLICT IGNORE" +
                ");";

        final String SQL_CREATE_LENDING_TABLE = "CREATE TABLE " + LendingEntry.TABLE_NAME + " (" +
                LendingEntry._ID + " INTEGER PRIMARY KEY, " +
                LendingEntry.COLUMN_BOOK_KEY + " INTEGER NOT NULL, " +
                LendingEntry.COLUMN_CONTACT_URI + " TEXT NOT NULL, " +
                LendingEntry.COLUMN_LENDING_DATE + " TEXT NOT NULL, " +

                // Set up the book foreign key
                " FOREIGN KEY (" + LendingEntry.COLUMN_BOOK_KEY + ") REFERENCES " +
                BookEntry.TABLE_NAME + "(" + BookEntry._ID + ")" +
                ");";

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_LENDING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
}
