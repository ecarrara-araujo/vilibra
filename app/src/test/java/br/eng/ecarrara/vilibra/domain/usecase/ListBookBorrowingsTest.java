package br.eng.ecarrara.vilibra.domain.usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import br.eng.ecarrara.vilibra.BuildConfig;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.domain.error.Error;
import br.eng.ecarrara.vilibra.domain.executor.Callback;
import br.eng.ecarrara.vilibra.domain.executor.Executor;
import br.eng.ecarrara.vilibra.domain.executor.MockCallbackThread;
import br.eng.ecarrara.vilibra.domain.executor.MockExecutor;
import br.eng.ecarrara.vilibra.domain.repository.BookBorrowingRepository;
import br.eng.ecarrara.vilibra.fakedata.BookBorrowingFixture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ListBookBorrowingsTest {

    private Executor executor;
    private ListBookBorrowings listBookBorrowings;
    private BookBorrowingRepository bookBorrowingRepository;
    private List<BookBorrowing> borrowedBooks;

    @Before public void setup() {
        this.bookBorrowingRepository = mock(BookBorrowingRepository.class);
        this.borrowedBooks = prepareBorrowedBooksList();
        when(this.bookBorrowingRepository.borrowedBooks()).thenReturn(this.borrowedBooks);
        this.executor = new MockExecutor();

        this.listBookBorrowings = new ListBookBorrowings(
                this.executor, bookBorrowingRepository);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecutionWithNullCallback() {
        this.listBookBorrowings.execute(null);
    }

    @Test public void testSuccessfulListBookBorrowingsUseCaseExecution() throws Exception {
        Callback<List<BookBorrowing>> callback = mock(Callback.class);
        when(callback.getCallbackThread()).thenReturn(new MockCallbackThread());
        this.listBookBorrowings.execute(callback);

        verify(callback).onFinished(this.borrowedBooks);
    }

    @Test public void testListBookBorrowingsUseCaseExecutionWithError() throws Exception {
        when(this.bookBorrowingRepository.borrowedBooks()).thenReturn(null);
        Callback<List<BookBorrowing>> callback = mock(Callback.class);
        when(callback.getCallbackThread()).thenReturn(new MockCallbackThread());

        this.listBookBorrowings.execute(callback);
        verify(callback).onError(any(Error.class));
    }

    private List<BookBorrowing> prepareBorrowedBooksList() {
        List<BookBorrowing> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(BookBorrowingFixture.getTestBookBorrowing());
        return borrowedBooks;
    }

}