package ecarrara.eng.vilibra;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ListView;

import com.shamanland.fab.FloatingActionButton;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = LendedBookListFragment.class.getSimpleName();

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Uri selectedLending);
    }

    private static final int LENDED_BOOK_LOADER = 0;

    private static final String[] LENDED_BOOKS_COLUMNS = {
        BookEntry.TABLE_NAME + "." + BookEntry._ID,
        BookEntry.COLUMN_TITLE,
        BookEntry.COLUMN_AUTHORS,
        BookEntry.COLUMN_PUBLISHER,
        LendingEntry.TABLE_NAME + "." + LendingEntry._ID,
        LendingEntry.COLUMN_CONTACT_URI
    };

    public static final int COL_BOOK_ID = 0;
    public static final int COL_BOOK_TITLE = 1;
    public static final int COL_BOOK_AUTHORS = 2;
    public static final int COL_BOOK_PUBLISHER = 3;
    public static final int COL_LENDING_ID = 4;
    public static final int COL_LENDING_CONTACT = 5;

    private LendedBookAdapter mLendedBookAdapter;
    private ListView mLendedBookListView;
    private static final String LIST_POSITION_KEY = "list_position";
    private int mListPosition;

    public LendedBookListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLendedBookAdapter = new LendedBookAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_lended_book_list, container, false);
        mLendedBookListView = (ListView) rootView.findViewById(R.id.lended_book_list_view);
        mLendedBookListView.setAdapter(mLendedBookAdapter);
        mLendedBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(LOG_TAG, "Lended book item clicked");
                Cursor cursor = mLendedBookAdapter.getCursor();
                if(null != cursor && cursor.moveToFirst()) {
                    cursor.moveToPosition(position);
                    Uri lendingUri = LendingEntry.buildLendingWithBookUri(
                            cursor.getLong(COL_LENDING_ID), cursor.getLong(COL_BOOK_ID));
                    ((Callback) getActivity()).onItemSelected(lendingUri);
                }
                mListPosition = position;
            }
        });

        View emptyView = rootView.findViewById(R.id.empty);
        mLendedBookListView.setEmptyView(emptyView);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) rootView.findViewById(R.id.add_lending_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LendedBookRegistrationActivity.class);
                startActivity(intent);
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(LIST_POSITION_KEY)) {
            mListPosition = savedInstanceState.getInt(LIST_POSITION_KEY);
        } else {
            mListPosition = 0;
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mListPosition != ListView.INVALID_POSITION) {
            outState.putInt(LIST_POSITION_KEY, mListPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LENDED_BOOK_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LENDED_BOOK_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                LendingEntry.buildLendingBooksUri(),
                LENDED_BOOKS_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLendedBookAdapter.swapCursor(cursor);
        if(mListPosition != ListView.INVALID_POSITION) {
            mLendedBookListView.smoothScrollToPosition(mListPosition);
            mLendedBookListView.setItemChecked(mListPosition, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLendedBookAdapter.swapCursor(null);
    }
}
