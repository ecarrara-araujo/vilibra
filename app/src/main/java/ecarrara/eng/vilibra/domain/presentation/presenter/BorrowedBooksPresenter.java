package ecarrara.eng.vilibra.domain.presentation.presenter;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ecarrara.eng.vilibra.ServiceLocator;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.presentation.view.BorrowedBooksListView;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;

/**
 * Presenter that controls the presentation of {@link BookBorrowing} on a
 * {@link BorrowedBooksListView}
 */
public class BorrowedBooksPresenter implements Presenter {

    private BorrowedBooksListView borrowedBooksListView;

    private Context context;
    private BorrowedBooksLoadAsyncTask borrowedBooksLoadAsyncTask;
    private BookBorrowingRepository bookBorrowingRepository;

    public BorrowedBooksPresenter(Context context, BorrowedBooksListView borrowedBooksListView) {
        this.context = context;
        this.borrowedBooksListView = borrowedBooksListView;
        this.bookBorrowingRepository = ServiceLocator.bookBorrowingRepository();
        this.borrowedBooksLoadAsyncTask = new BorrowedBooksLoadAsyncTask();
    }

    public void initialize() {
        this.loadBorrowedBooksList();
    }

    @Override public void resume() { initialize(); }

    @Override public void pause() { cancelGetBorrowedBooksListTask(); }

    @Override public void destroy() { }

    private void loadBorrowedBooksList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getBorrowedBooksList();
    }

    private void getBorrowedBooksList() {
        if (this.borrowedBooksLoadAsyncTask.getStatus() != AsyncTask.Status.RUNNING) {
            this.borrowedBooksLoadAsyncTask = new BorrowedBooksLoadAsyncTask();
            this.borrowedBooksLoadAsyncTask.execute();
        }
    }

    private void cancelGetBorrowedBooksListTask() {
        if (this.borrowedBooksLoadAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            this.borrowedBooksLoadAsyncTask.cancel(true);
        }
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

    class BorrowedBooksLoadAsyncTask extends AsyncTask<Void, Void, List<BookBorrowing>> {
        @Override protected List<BookBorrowing> doInBackground(Void... voids) {
            return BorrowedBooksPresenter.this.bookBorrowingRepository.borrowedBooks();
        }

        @Override protected void onPostExecute(List<BookBorrowing> bookBorrowingsList) {
            BorrowedBooksPresenter.this.displayBorrowedBooksList(bookBorrowingsList);
        }
    }

}
