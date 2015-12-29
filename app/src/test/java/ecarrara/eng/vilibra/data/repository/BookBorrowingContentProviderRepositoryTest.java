package ecarrara.eng.vilibra.data.repository;

import android.content.Context;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.domain.entity.BookBorrowing;
import ecarrara.eng.vilibra.domain.repository.BookBorrowingRepository;
import ecarrara.eng.vilibra.test.fixture.VilibraProviderFixture;
import ecarrara.eng.vilibra.utils.RobolectricUtils;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookBorrowingContentProviderRepositoryTest {

    private Context context;
    private VilibraProviderFixture vilibraProviderFixture;

    private BookBorrowing testBookBorrowing;
    private BookBorrowingRepository bookBorrowingRepository;

    @BeforeClass public static void setupRobolectric() {
        RobolectricUtils.redirectLogsToSystemOutput();
        RobolectricUtils.setupVilibraContentProvider();
    }

    @Before public void setup() {
        this.context = RuntimeEnvironment.application;
        this.vilibraProviderFixture = new VilibraProviderFixture(this.context);
        this.vilibraProviderFixture.prepareTestProvider();
        this.testBookBorrowing = this.vilibraProviderFixture.getDevsTestBookBorrowing();

        this.bookBorrowingRepository = new BookBorrowingContentProviderRepository(
                context.getContentResolver());
    }

    @Test public void testOneBorrowedBook() throws Exception {
        List<BookBorrowing> borrowedBooks =  this.bookBorrowingRepository.borrowedBooks();
        assertThat(borrowedBooks.size(), is(1));

        BookBorrowing bookBorrowing = borrowedBooks.get(0);
        assertThat(bookBorrowing, equalTo(this.testBookBorrowing));
    }

    @Test public void testAddOneBookBorrowing() throws Exception {
        this.context.getContentResolver().delete(LendingEntry.CONTENT_URI, null, null);
        List<BookBorrowing> borrowedBooks =  this.bookBorrowingRepository.borrowedBooks();
        assertThat(borrowedBooks.size(), is(0));

        this.bookBorrowingRepository.addBookBorrowing(this.testBookBorrowing);
        testOneBorrowedBook();
    }

    @Test public void testRemoveBookBorrowing() throws Exception {
        this.bookBorrowingRepository.removeBookBorrowing(this.testBookBorrowing);
        List<BookBorrowing> borrowedBooks =  this.bookBorrowingRepository.borrowedBooks();
        assertThat(borrowedBooks.size(), is(0));
    }
}