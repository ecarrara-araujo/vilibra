package br.eng.ecarrara.vilibra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.android.presentation.LoanedBookListAdapter;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.domain.presentation.presenter.BorrowedBooksPresenter;
import br.eng.ecarrara.vilibra.domain.presentation.view.BorrowedBooksListView;

import static android.Manifest.permission.CAMERA;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static android.support.v4.content.PermissionChecker.checkCallingOrSelfPermission;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookListFragment extends Fragment implements
        BorrowedBooksListView {

    private static final String LOG_TAG = LendedBookListFragment.class.getSimpleName();

    private static final int CAMERA_PERMISSION_REQUEST_ID = 1000;

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

    private RecyclerView loanedBookListView;
    private RecyclerView.LayoutManager loanedBookListViewLayoutManager;
    private LoanedBookListAdapter loanedBookListViewAdapter;
    private View emptyLoanedBooksListView;

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

    @Override public void render(List<BookBorrowing> borrowedBooks) {
        this.loanedBookListViewAdapter.setLoanedBooks(borrowedBooks);

        // TODO: improve this, maybe this should be done by the presenter
        if(this.loanedBookListViewAdapter.getItemCount() <= 0) {
            this.loanedBookListView.setVisibility(View.GONE);
            this.emptyLoanedBooksListView.setVisibility(View.VISIBLE);
        } else {
            this.loanedBookListView.setVisibility(View.VISIBLE);
            this.emptyLoanedBooksListView.setVisibility(View.GONE);
            this.loanedBookListView.smoothScrollToPosition(
                    this.loanedBookListViewAdapter.getCurrentListPosition());
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
        this.loanedBookListViewAdapter = new LoanedBookListAdapter(getContext());
        this.loanedBookListViewAdapter.setOnItemClickListener(
                (LoanedBookListAdapter.OnItemClickListener) this.getActivity());
        this.loanedBookListViewLayoutManager = new LinearLayoutManager(this.getContext());

        this.loanedBookListView = (RecyclerView) rootView.findViewById(R.id.loaned_book_list_view);
        this.loanedBookListView.setHasFixedSize(true); // for performance
        this.loanedBookListView.setLayoutManager(loanedBookListViewLayoutManager);
        this.loanedBookListView.setAdapter(loanedBookListViewAdapter);

        this.emptyLoanedBooksListView = rootView.findViewById(R.id.empty);

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) rootView.findViewById(R.id.add_lending_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCameraUsagePermitted()) {
                    Intent intent = new Intent(getActivity(), LendedBookRegistrationActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions(
                            new String[]{CAMERA},
                            CAMERA_PERMISSION_REQUEST_ID
                    );
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    Intent intent = new Intent(getActivity(), LendedBookRegistrationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LendedBookListFragment.this.getContext(), R.string.lendedBookList_toast_camera_permission, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private boolean isCameraUsagePermitted() {
        return checkCallingOrSelfPermission(LendedBookListFragment.this.getContext(), CAMERA)
                == PERMISSION_GRANTED;
    }

    private void restoreState(Bundle savedInstanceState) {
        if(savedInstanceState != null && savedInstanceState.containsKey(LIST_POSITION_KEY)) {
            mListPosition = savedInstanceState.getInt(LIST_POSITION_KEY);
        } else {
            mListPosition = 0;
        }
    }

    private void initialize() {
        this.borrowedBooksPresenter = new BorrowedBooksPresenter(
                this.getContext(), this,
                ServiceLocator.bookBorrowingRepository(), ServiceLocator.executor());
    }

}
