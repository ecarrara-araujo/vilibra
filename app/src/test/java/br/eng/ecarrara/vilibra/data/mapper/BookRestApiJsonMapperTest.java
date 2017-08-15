package br.eng.ecarrara.vilibra.data.mapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import br.eng.ecarrara.vilibra.BuildConfig;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.fixture.BookFixture;
import br.eng.ecarrara.vilibra.util.DefaultData;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDbDateString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class BookRestApiJsonMapperTest {

    private BookRestApiJsonMapper mapper;

    @Before public void configureMapper() {
        this.mapper = new BookRestApiJsonMapper();
    }

    @Test public void testTransformNullBookVolumeToBook() throws Exception {
        Book transformedBook = mapper.transform(null);
        assertThat(transformedBook, equalTo(Book.Companion.getNO_BOOK()));
    }

    @Test public void testTransformBookVolumeToBook() {
        Book expectedBook = BookFixture.INSTANCE.getTestBookDevsTestBook();

        BookVolume bookVolume = mock(BookVolume.class);
        BookVolume.BookVolumeInfo bookVolumeInfo = mock(BookVolume.BookVolumeInfo.class);

        when(bookVolume.getVolumeInfo()).thenReturn(bookVolumeInfo);

        when(bookVolumeInfo.getTitle()).thenReturn(expectedBook.getTitle());
        when(bookVolumeInfo.getSubtitle()).thenReturn(expectedBook.getSubtitle());
        when(bookVolumeInfo.getAuthors()).thenReturn(expectedBook.getAuthors());
        when(bookVolumeInfo.getPublisher()).thenReturn(expectedBook.getPublisher());
        when((bookVolumeInfo.getPublishedDate()))
                .thenReturn(getDbDateString(expectedBook.getPublishedDate()));
        when(bookVolumeInfo.getISBN10()).thenReturn(expectedBook.getIsbn10());
        when(bookVolumeInfo.getISBN13()).thenReturn(expectedBook.getIsbn13());
        when(bookVolumeInfo.getPageCount()).thenReturn(expectedBook.getPageCount());

        Book transformedBook = this.mapper.transform(bookVolume);

        assertThat(transformedBook, equalTo(expectedBook));
    }

    @Test public void testTransformBookVolumeToBookWithInvalidDate() {
        BookVolume bookVolume = mock(BookVolume.class);
        BookVolume.BookVolumeInfo bookVolumeInfo = mock(BookVolume.BookVolumeInfo.class);

        when(bookVolume.getVolumeInfo()).thenReturn(bookVolumeInfo);
        when((bookVolumeInfo.getPublishedDate())).thenReturn("INVALID"); //invalid date string

        Book transformedBook = this.mapper.transform(bookVolume);

        assertThat(transformedBook.getPublishedDate(), equalTo(DefaultData.NOT_INITIALIZED.getDate()));
    }
    
}