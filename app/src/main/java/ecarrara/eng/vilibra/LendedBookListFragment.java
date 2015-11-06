package ecarrara.eng.vilibra;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.domain.presentation.presenter.BorrowedBooksPresenter;
import ecarrara.eng.vilibra.domain.presentation.view.BorrowedBooksListView;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookListFragment extends Fragment implements
        BorrowedBooksListView {

    private static final String LOG_TAG = LendedBookListFragment.class.getSimpleName();

    private BorrowedBooksPresenter borrowedBooksPresenter;

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

    private LendedBookAdapter mLendedBookAdapter;
    private ListView mLendedBookListView;
    private static final String LIST_POSITION_KEY = "list_position";
    private int mListPosition;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lended_book_list, container, false);
        this.setupUI(rootView);
        this.restoreState(savedInstanceState);
        return rootView;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
    }

    @Override public void onPause() {
        super.onPause();
        this.borrowedBooksPresenter.pause();
    }

    @Override public void onResume() {
        super.onResume();
        this.borrowedBooksPresenter.resume();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        this.borrowedBooksPresenter.destroy();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        if(mListPosition != ListView.INVALID_POSITION) {
            outState.putInt(LIST_POSITION_KEY, mListPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override public void render(Cursor borrowedBooks) {
        mLendedBookAdapter.swapCursor(borrowedBooks);
        if(mListPosition != ListView.INVALID_POSITION) {
            mLendedBookListView.smoothScrollToPosition(mListPosition);
            mLendedBookListView.setItemChecked(mListPosition, true);
        }
    }

    @Override public void showLoading() {
        // did not have this treatment before
    }

    @Override public void hideLoading() {
        // did not have this treatment before
    }

    @Override public void showRetry() {
        // did not have this treatment before
    }

    @Override public void hideRetry() {
        // did not have this treatment before
    }

    @Override public void showError(String message) {
        // did not have this treatment before
    }

    private void setupUI(View rootView) {
        mLendedBookAdapter = new LendedBookAdapter(getActivity(), null, 0);
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
                            cursor.getLong(BorrowedBooksPresenter.COL_LENDING_ID),
                            cursor.getLong(BorrowedBooksPresenter.COL_BOOK_ID));
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
    }

    private void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(LIST_POSITION_KEY)) {
            mListPosition = savedInstanceState.getInt(LIST_POSITION_KEY);
        } else {
            mListPosition = 0;
        }
    }

    private void initialize() {
        this.borrowedBooksPresenter = new BorrowedBooksPresenter(this.getContext(), this);
        this.borrowedBooksPresenter.initialize();
    }

}
