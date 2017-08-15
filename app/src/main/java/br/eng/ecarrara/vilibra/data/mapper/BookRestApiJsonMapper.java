package br.eng.ecarrara.vilibra.data.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.util.DefaultData;

/**
 * Mapper class to transform a {@link br.eng.ecarrara.vilibra.model.BookVolume} containing book
 * information retrieved from a REST Service to a {@link Book} from the domain.
 */
public class BookRestApiJsonMapper {

    public static final String JSON_DATE_FORMAT = "yyyyMMdd";

    public Book transform(BookVolume bookVolume) {
        if(bookVolume == null) {
            return Book.Companion.getNO_BOOK();
        }

        BookVolume.BookVolumeInfo bookVolumeInfo = bookVolume.getVolumeInfo();
        Book book = new Book(VilibraContract.ENTRY_NOT_SAVED_ID,
                bookVolumeInfo.getTitle(), bookVolumeInfo.getSubtitle(), bookVolumeInfo.getAuthors(),
                bookVolumeInfo.getPublisher(), transformJsonPublishedDate(bookVolumeInfo.getPublishedDate()),
                bookVolumeInfo.getPageCount(), bookVolumeInfo.getISBN10(), bookVolumeInfo.getISBN13());

        return book;
    }

    private Date transformJsonPublishedDate(String publishedDate) {
        Date convertedPublishedDate = DefaultData.NOT_INITIALIZED.getDate();
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat(JSON_DATE_FORMAT);

        try {
            convertedPublishedDate = jsonDateFormat.parse(publishedDate);
        } catch ( ParseException e ) {
            e.printStackTrace();
        }

        return convertedPublishedDate;
    }

}
