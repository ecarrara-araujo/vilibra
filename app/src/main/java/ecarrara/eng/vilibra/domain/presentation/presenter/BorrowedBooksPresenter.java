package ecarrara.eng.vilibra.domain.presentation.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.presentation.view.BorrowedBooksListView;
/**
 * Presenter that controls the presentation of {@link BookBorrowing} on a
 * {@link BorrowedBooksListView}
 */
public class BorrowedBooksPresenter implements Presenter {

    private BorrowedBooksListView borrowedBooksListView;

    private Context context;
    private BorrowedBooksLoadAsyncTask borrowedBooksLoadAsyncTask;

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

    public BorrowedBooksPresenter(Context context, BorrowedBooksListView borrowedBooksListView) {
        this.context = context;
        this.borrowedBooksListView = borrowedBooksListView;
        this.borrowedBooksLoadAsyncTask = new BorrowedBooksLoadAsyncTask();
    }

    public void initialize() {
        this.loadBorrowedBooksList();
    }

    @Override public void resume() {
        this.loadBorrowedBooksList();
    }

    @Override public void pause() { /* no-op */ }

    @Override public void destroy() {
        this.borrowedBooksLoadAsyncTask.cancel(true);
    }

    private void loadBorrowedBooksList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getBorrowedBooksList();
    }

    private void getBorrowedBooksList() {
        new BorrowedBooksLoadAsyncTask().execute();
    }

    private void showViewLoading() {
        this.borrowedBooksListView.showLoading();
    }

    private void hideViewLoading() {
        this.borrowedBooksListView.hideLoading();
    }

    private void hideViewRetry() {
        this.borrowedBooksListView.hideRetry();
    }

    private Cursor getBorrowedBooksFromContentProvider() {
        Cursor borrowedBooksCursor = context.getContentResolver().query(
                LendingEntry.buildLendingBooksUri(),
                LENDED_BOOKS_COLUMNS,
                null,
                null,
                null
        );
        return borrowedBooksCursor;
    }

    private void displayBorrowedBooksList(Cursor borrowedBooksCursor) {
        this.borrowedBooksListView.render(borrowedBooksCursor);
        this.hideViewLoading();
    }

    class BorrowedBooksLoadAsyncTask extends AsyncTask<Void, Void, Cursor> {
        @Override protected Cursor doInBackground(Void... voids) {
            return BorrowedBooksPresenter.this.getBorrowedBooksFromContentProvider();
        }

        @Override protected void onPostExecute(Cursor cursor) {
            BorrowedBooksPresenter.this.displayBorrowedBooksList(cursor);
        }
    }

}
