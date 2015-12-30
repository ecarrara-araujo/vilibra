package ecarrara.eng.vilibra.domain.presentation.presenter;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ecarrara.eng.vilibra.ServiceLocator;
import ecarrara.eng.vilibra.android.executor.AndroidCallbackThread;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.error.Error;
import ecarrara.eng.vilibra.domain.executor.Callback;
import ecarrara.eng.vilibra.domain.executor.CallbackThread;
import ecarrara.eng.vilibra.domain.executor.Executor;
import ecarrara.eng.vilibra.domain.presentation.view.BorrowedBooksListView;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;
import ecarrara.eng.vilibra.domain.usecase.ListBookBorrowings;

/**
 * Presenter that controls the presentation of {@link BookBorrowing} on a
 * {@link BorrowedBooksListView}
 */
public class BorrowedBooksPresenter implements Presenter {

    private BorrowedBooksListView borrowedBooksListView;

    private Context context;
    private ListBookBorrowings listBookBorrowings;
    private Callback<List<BookBorrowing>> listBookBorrowingsCallback;

    public BorrowedBooksPresenter(
            Context context,
            BorrowedBooksListView borrowedBooksListView,
            BookBorrowingRepository bookBorrowingRepository,
            Executor executor) {
        this.context = context;
        this.borrowedBooksListView = borrowedBooksListView;
        this.listBookBorrowingsCallback = new ListBookBorrowingsCallback();
        this.listBookBorrowings = new ListBookBorrowings(
                executor,
                bookBorrowingRepository);
    }

    public void initialize() {
        this.loadBorrowedBooksList();
    }

    @Override public void resume() { initialize(); }

    @Override public void pause() { }

    @Override public void destroy() { }

    private void loadBorrowedBooksList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getBorrowedBooksList();
    }

    private void getBorrowedBooksList() {
        this.listBookBorrowings.execute(this.listBookBorrowingsCallback);
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

    private void displayBorrowedBooksList(List<BookBorrowing> borrowedBooksCursor) {
        this.borrowedBooksListView.render(borrowedBooksCursor);
        this.hideViewLoading();
    }

    class ListBookBorrowingsCallback implements Callback<List<BookBorrowing>> {

        private AndroidCallbackThread androidCallbackThread;

        public ListBookBorrowingsCallback() {
            this.androidCallbackThread = new AndroidCallbackThread();
        }

        @Override public CallbackThread getCallbackThread() {
            return this.androidCallbackThread;
        }

        @Override public void onFinished(List<BookBorrowing> processed) {
            BorrowedBooksPresenter.this.displayBorrowedBooksList(processed);
        }

        @Override public void onError(Error error) {
            /* no-op */
        }
    }
}
