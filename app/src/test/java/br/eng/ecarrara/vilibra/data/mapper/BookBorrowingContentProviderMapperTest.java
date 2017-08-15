package br.eng.ecarrara.vilibra.data.mapper;

import android.content.ContentValues;
import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.List;

import br.eng.ecarrara.vilibra.BuildConfig;
import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.data.VilibraContract.LendingEntry;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.fixture.BookBorrowingFixture;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDateFromDb;
import static br.eng.ecarrara.vilibra.data.VilibraContract.getDbDateString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookBorrowingContentProviderMapperTest {

    private BookBorrowingContentProviderMapper bookBorrowingContentProviderMapper;
    private BookBorrowing testBookBorrowing;

    @Before public void prepareTest() {
        this.testBookBorrowing = BookBorrowingFixture.getTestBookBorrowing();
        this.bookBorrowingContentProviderMapper = new BookBorrowingContentProviderMapper();
    }

    @Test public void testTransformEmptyCursorToBookBorrowing() throws Exception {
        Cursor cursor = mock(Cursor.class);
        when(cursor.moveToFirst()).thenReturn(false);

        List<BookBorrowing> bookBorrowings =
                this.bookBorrowingContentProviderMapper.transform(cursor);

        assertThat(bookBorrowings, is(empty()));
    }

    @Test public void testTransformNullCursorToBookBorrowing() throws Exception {
        Cursor cursor = null;

        List<BookBorrowing> bookBorrowings =
                this.bookBorrowingContentProviderMapper.transform(cursor);

        assertThat(bookBorrowings, is(empty()));
    }

    @Test public void testTransformCursorToBookBorrowing() throws Exception {
        Cursor cursor = prepareMockCursor();

        List<BookBorrowing> bookBorrowings
                = this.bookBorrowingContentProviderMapper.transform(cursor);

        assertThat(bookBorrowings.size(), is(1));

        BookBorrowing transformedBookBorrowing = bookBorrowings.get(0);
        assertThat(transformedBookBorrowing, equalTo(this.testBookBorrowing));
    }

    @Test public void testTransformBookBorrowingToContentValues() throws Exception {
        ContentValues generatedContentValues =
                this.bookBorrowingContentProviderMapper.transform(this.testBookBorrowing);

        long bookId = generatedContentValues.getAsLong(LendingEntry.COLUMN_BOOK_ID);
        String borrowerContactUri =
                generatedContentValues.getAsString(LendingEntry.COLUMN_CONTACT_URI);
        Date borrowingDate =
                getDateFromDb(generatedContentValues.getAsString(LendingEntry.COLUMN_LENDING_DATE));

        assertThat(bookId, equalTo(this.testBookBorrowing.getBorrowedBook().getId()));
        assertThat(borrowerContactUri,
                equalTo(this.testBookBorrowing.getBorrower().getContactInformationUri()));
        assertThat(borrowingDate, equalTo(this.testBookBorrowing.getLendingDate()));
    }

    private Cursor prepareMockCursor() {
        Cursor cursor = mock(Cursor.class);

        final int INDEX_COLUMN_BOOK_ID = 0;
        final int INDEX_COLUMN_CONTACT_URI = 1;
        final int INDEX_COLUMN_LENDING_DATE = 2;
        final int INDEX_COLUMN_TITLE = 3;
        final int INDEX_COLUMN_SUBTITLE = 4;
        final int INDEX_COLUMN_AUTHORS = 5;
        final int INDEX_COLUMN_PUBLISHER = 6;
        final int INDEX_COLUMN_PUBLISHED_DATE = 7;
        final int INDEX_COLUMN_ISBN_10 = 8;
        final int INDEX_COLUMN_ISBN_13 = 9;
        final int INDEX_COLUMN_PAGES = 10;

        when(cursor.moveToFirst()).thenReturn(true);

        // mocking column indexes
        when(cursor.getColumnIndex(LendingEntry.COLUMN_BOOK_ID))
                .thenReturn(INDEX_COLUMN_BOOK_ID);
        when(cursor.getColumnIndex(LendingEntry.COLUMN_CONTACT_URI))
                .thenReturn(INDEX_COLUMN_CONTACT_URI);
        when(cursor.getColumnIndex(LendingEntry.COLUMN_LENDING_DATE))
                .thenReturn(INDEX_COLUMN_LENDING_DATE);
        when(cursor.getColumnIndex(BookEntry.COLUMN_TITLE)).thenReturn(INDEX_COLUMN_TITLE);
        when(cursor.getColumnIndex(BookEntry.COLUMN_SUBTITLE)).thenReturn(INDEX_COLUMN_SUBTITLE);
        when(cursor.getColumnIndex(BookEntry.COLUMN_AUTHORS)).thenReturn(INDEX_COLUMN_AUTHORS);
        when(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHER)).thenReturn(INDEX_COLUMN_PUBLISHER);
        when(cursor.getColumnIndex(BookEntry.COLUMN_PUBLISHED_DATE))
                .thenReturn(INDEX_COLUMN_PUBLISHED_DATE);
        when(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_10)).thenReturn(INDEX_COLUMN_ISBN_10);
        when(cursor.getColumnIndex(BookEntry.COLUMN_ISBN_13)).thenReturn(INDEX_COLUMN_ISBN_13);
        when(cursor.getColumnIndex(BookEntry.COLUMN_PAGES)).thenReturn(INDEX_COLUMN_PAGES);

        //mocking values reading calls
        when(cursor.getLong(INDEX_COLUMN_BOOK_ID))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getId());
        when(cursor.getString(INDEX_COLUMN_CONTACT_URI))
                .thenReturn(this.testBookBorrowing.getBorrower().getContactInformationUri());
        when(cursor.getString(INDEX_COLUMN_LENDING_DATE))
                .thenReturn(getDbDateString(this.testBookBorrowing.getLendingDate()));
        when(cursor.getString(INDEX_COLUMN_TITLE))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getTitle());
        when(cursor.getString(INDEX_COLUMN_SUBTITLE))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getSubtitle());
        when(cursor.getString(INDEX_COLUMN_AUTHORS))
                .thenReturn(AuthorsListMapper.transformAuthorsListToDatabaseFormat(
                        this.testBookBorrowing.getBorrowedBook().getAuthors()));
        when(cursor.getString(INDEX_COLUMN_PUBLISHER))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getPublisher());
        when(cursor.getString(INDEX_COLUMN_PUBLISHED_DATE))
                .thenReturn(getDbDateString(this.testBookBorrowing.getBorrowedBook().getPublishedDate()));
        when(cursor.getString(INDEX_COLUMN_ISBN_10))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getIsbn10());
        when(cursor.getString(INDEX_COLUMN_ISBN_13))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getIsbn13());
        when(cursor.getInt(INDEX_COLUMN_PAGES))
                .thenReturn(this.testBookBorrowing.getBorrowedBook().getPageCount());

        return cursor;
    }

}