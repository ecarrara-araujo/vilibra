package ecarrara.eng.vilibra.domain.presentation.presenter;

import android.content.Context;
import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.executor.Executor;
import ecarrara.eng.vilibra.domain.executor.MockExecutor;
import ecarrara.eng.vilibra.domain.presentation.view.BorrowedBooksListView;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BorrowedBooksPresenterTest {

    private BorrowedBooksPresenter borrowedBooksPresenter;
    private BorrowedBooksListView borrowedBooksListView;

    private Context context;

    @Before
    public void setUp() {
        this.context = RuntimeEnvironment.application;
        this.borrowedBooksListView = mock(BorrowedBooksListView.class);

        Executor executor = new MockExecutor();
        BookBorrowingRepository bookBorrowingRepository = mock(BookBorrowingRepository.class);
        when(bookBorrowingRepository.borrowedBooks()).thenReturn(new ArrayList<BookBorrowing>());

        this.borrowedBooksPresenter = new BorrowedBooksPresenter(
                context, borrowedBooksListView, bookBorrowingRepository, executor);
    }

    @Test
    public void testInitialize() throws Exception {
        this.borrowedBooksPresenter.initialize();
        verifyBorrowedBooksListRender();
    }

    @Test
    public void testResume() throws Exception {
        this.borrowedBooksPresenter.resume();
        verifyBorrowedBooksListRender();
    }

    @Test
    public void testPause() throws Exception {
        this.borrowedBooksPresenter.pause();
        verifyZeroInteractions(this.borrowedBooksListView);
    }

    @Test
    public void testDestroy() throws Exception {
        this.borrowedBooksPresenter.destroy();
        verifyZeroInteractions(this.borrowedBooksListView);
    }

    private void verifyBorrowedBooksListRender() {
        verify(this.borrowedBooksListView).hideRetry();
        verify(this.borrowedBooksListView).showLoading();
        verify(this.borrowedBooksListView).render(any(List.class));
        verify(this.borrowedBooksListView).hideLoading();
    }
}