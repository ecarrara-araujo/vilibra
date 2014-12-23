package ecarrara.eng.vilibra;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;

/**
 * Created by ecarrara on 23/12/2014.
 */
public class LendedBookDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks <Cursor> {

    private static final String LOG_TAG = LendedBookDetailFragment.class.getSimpleName();
    private static final int LENDING_DETAIL_LOADER = 0;

    private CursorLoader mLendingInfoLoader;

    private static final String[] LENDED_BOOKS_COLUMNS = {
            BookEntry.TABLE_NAME + "." + VilibraContract.BookEntry._ID,
            BookEntry.COLUMN_TITLE,
            BookEntry.COLUMN_SUBTITLE,
            BookEntry.COLUMN_AUTHORS,
            BookEntry.COLUMN_PUBLISHER,
            BookEntry.COLUMN_ISBN_10,
            BookEntry.COLUMN_ISBN_13,
            BookEntry.COLUMN_PAGES,
            BookEntry.COLUMN_PUBLISHED_DATE,
            LendingEntry.TABLE_NAME + "." + VilibraContract.LendingEntry._ID,
            LendingEntry.COLUMN_CONTACT_URI,
            LendingEntry.COLUMN_LENDING_DATE
    };

    public static final int COL_BOOK_ID = 0;
    public static final int COL_BOOK_TITLE = 1;
    public static final int COL_BOOK_SUBTITLE = 2;
    public static final int COL_BOOK_AUTHORS = 3;
    public static final int COL_BOOK_PUBLISHER = 4;
    public static final int COL_BOOK_ISBN_10 = 5;
    public static final int COL_BOOK_ISBN_13 = 6;
    public static final int COL_BOOK_PAGES = 7;
    public static final int COL_BOOK_PUBLISHED_DATE = 8;
    public static final int COL_LENDING_ID = 9;
    public static final int COL_LENDING_CONTACT = 10;
    public static final int COL_LENDING_DATE = 11;

    public LendedBookDetailFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lended_book_detail, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLoaderManager().restartLoader(LENDING_DETAIL_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null
                && arguments.containsKey(LendedBookDetailActivity.EXTRA_KEY_BOOK_LENDING_URI)) {
            getLoaderManager().initLoader(LENDING_DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");

        Uri lendingUri =
                getArguments().getParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_LENDING_URI);
        return new CursorLoader(getActivity(), lendingUri, LENDED_BOOKS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if(!cursor.moveToFirst()) { return; }

        String bookTitle = cursor.getString(COL_BOOK_TITLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
