package ecarrara.eng.vilibra.data.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.domain.entity.Book;
import ecarrara.eng.vilibra.model.BookVolume;

/**
 * Mapper class to transform a {@link ecarrara.eng.vilibra.model.BookVolume} containing book
 * information retrieved from a REST Service to a {@link Book} from the domain.
 */
public class BookRestApiJsonMapper {

    public static final String JSON_DATE_FORMAT = "yyyyMMdd";

    public Book transform(BookVolume bookVolume) {
        if(bookVolume == null) {
            return Book.NO_BOOK;
        }

        BookVolume.BookVolumeInfo bookVolumeInfo = bookVolume.getVolumeInfo();
        Book book = new Book.Builder(VilibraContract.ENTRY_NOT_SAVED_ID, bookVolumeInfo.getTitle())
                .setSubtitle(bookVolumeInfo.getSubtitle())
                .setAuthors(bookVolumeInfo.getAuthors())
                .setPublisher(bookVolumeInfo.getPublisher())
                .setPublishedDate(transformJsonPublishedDate(bookVolumeInfo.getPublishedDate()))
                .setIsbn10(bookVolumeInfo.getISBN10())
                .setIsbn13(bookVolumeInfo.getISBN13())
                .setPageCount(bookVolumeInfo.getPageCount())
                .build();

        return book;
    }

    private Date transformJsonPublishedDate(String publishedDate) {
        Date convertedPublishedDate = Book.DATE_NOT_INFORMED;
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);

        try {
            convertedPublishedDate = jsonDateFormat.parse(publishedDate);
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        return convertedPublishedDate;
    }

}
