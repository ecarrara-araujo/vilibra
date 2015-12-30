package ecarrara.eng.vilibra.domain.usecase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.error.Error;
import ecarrara.eng.vilibra.domain.executor.Callback;
import ecarrara.eng.vilibra.domain.executor.Executor;
import ecarrara.eng.vilibra.domain.executor.MockCallbackThread;
import ecarrara.eng.vilibra.domain.executor.MockExecutor;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;
import ecarrara.eng.vilibra.test.fixture.BookBorrowingFixture;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
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
        verify(callback).onError(anyError());
    }

    private List<BookBorrowing> prepareBorrowedBooksList() {
        List<BookBorrowing> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(BookBorrowingFixture.getTestBookBorrowing());
        return borrowedBooks;
    }

    static Error anyError() {
        return argThat(new AnyError());
    }

    static class AnyError implements ArgumentMatcher<Error> {
        @Override public boolean matches(Object argument) {
            return (argument instanceof Error);
        }
    }
}