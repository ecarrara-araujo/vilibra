package ecarrara.eng.vilibra.data.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.mock.MockCursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.List;

import ecarrara.eng.vilibra.BuildConfig;
import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.test.fixture.BookFixture;

import static ecarrara.eng.vilibra.data.VilibraContract.getDateFromDb;
import static ecarrara.eng.vilibra.data.VilibraContract.getDbDateString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookContentProviderMapperTest {

    private BookContentProviderMapper mapper;
    private Book testBook;

    @Before public void prepareTest() {
        this.mapper = new BookContentProviderMapper();
        this.testBook = BookFixture.getTestBookDevsTestBook();
    }

    @Test public void testTransformEmptyCursorToBook() throws Exception {
        Cursor cursor = mock(MockCursor.class);

        when(cursor.isBeforeFirst()).thenReturn(true);
        when(cursor.isAfterLast()).thenReturn(false);
        Book transformedBook = this.mapper.transform(cursor);
        assertThat(transformedBook, equalTo(Book.NO_BOOK));

        when(cursor.isBeforeFirst()).thenReturn(false);
        when(cursor.isAfterLast()).thenReturn(true);
        transformedBook = this.mapper.transform(cursor);
        assertThat(transformedBook, equalTo(Book.NO_BOOK));
    }

    @Test @SuppressWarnings({"ConstantConditions"}) public void testTransformNullCursorToBook() throws Exception {
        Cursor cursor = null;
        Book transformedBook = this.mapper.transform(cursor);
        assertThat(transformedBook, equalTo(Book.NO_BOOK));
    }

    @Test public void testTransformCursorToBook() throws Exception {
        Cursor cursor = mock(MockCursor.class);

        final int INDEX_COLUMN_TITLE = 0;
        final int INDEX_COLUMN_SUBTITLE = 1;
        final int INDEX_COLUMN_AUTHORS = 2;
        final int INDEX_COLUMN_PUBLISHER = 3;
        final int INDEX_COLUMN_PUBLISHED_DATE = 4;
        final int INDEX_COLUMN_ISBN_10 = 5;
        final int INDEX_COLUMN_ISBN_13 = 6;
        final int INDEX_COLUMN_PAGES = 7;

        when(cursor.moveToFirst()).thenReturn(true);

        // mocking column indexes
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
        when(cursor.getString(INDEX_COLUMN_TITLE)).thenReturn(this.testBook.getTitle());
        when(cursor.getString(INDEX_COLUMN_SUBTITLE)).thenReturn(this.testBook.getSubtitle());
        when(cursor.getString(INDEX_COLUMN_AUTHORS))
                .thenReturn(AuthorsListMapper.transformAuthorsListToDatabaseFormat(this.testBook.getAuthors()));
        when(cursor.getString(INDEX_COLUMN_PUBLISHER)).thenReturn(this.testBook.getPublisher());
        when(cursor.getString(INDEX_COLUMN_PUBLISHED_DATE))
                .thenReturn(getDbDateString(this.testBook.getPublishedDate()));
        when(cursor.getString(INDEX_COLUMN_ISBN_10)).thenReturn(this.testBook.getIsbn10());
        when(cursor.getString(INDEX_COLUMN_ISBN_13)).thenReturn(this.testBook.getIsbn13());
        when(cursor.getInt(INDEX_COLUMN_PAGES)).thenReturn(this.testBook.getPageCount());

        Book transformedBook = this.mapper.transform(cursor);

        assertThat(transformedBook, equalTo(this.testBook));
    }

    @Test public void testTransformBookToContentValues() throws Exception {
        ContentValues generatedContentValues = this.mapper.transform(this.testBook);

        String title = generatedContentValues.getAsString(BookEntry.COLUMN_TITLE);
        String subtitle = generatedContentValues.getAsString(BookEntry.COLUMN_SUBTITLE);
        List<String> authors =
                AuthorsListMapper.transformAuthorsFromCommaSeparatedList(
                        generatedContentValues.getAsString(BookEntry.COLUMN_AUTHORS));
        String publisher = generatedContentValues.getAsString(BookEntry.COLUMN_PUBLISHER);
        Date publishedDate =
                getDateFromDb(generatedContentValues.getAsString(BookEntry.COLUMN_PUBLISHED_DATE));
        String isbn10 = generatedContentValues.getAsString(BookEntry.COLUMN_ISBN_10);
        String isbn13 = generatedContentValues.getAsString(BookEntry.COLUMN_ISBN_13);
        int numberOfPages = generatedContentValues.getAsInteger(BookEntry.COLUMN_PAGES);

        assertThat(title, equalTo(this.testBook.getTitle()));
        assertThat(subtitle, equalTo(this.testBook.getSubtitle()));
        assertThat(authors, equalTo(this.testBook.getAuthors()));
        assertThat(publisher, equalTo(this.testBook.getPublisher()));
        assertThat(publishedDate, equalTo(this.testBook.getPublishedDate()));
        assertThat(isbn10, equalTo(this.testBook.getIsbn10()));
        assertThat(isbn13, equalTo(this.testBook.getIsbn13()));
        assertThat(numberOfPages, equalTo(this.testBook.getPageCount()));
    }
    
}