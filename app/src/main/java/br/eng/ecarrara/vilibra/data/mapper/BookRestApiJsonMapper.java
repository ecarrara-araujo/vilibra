package br.eng.ecarrara.vilibra.data.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolumeInfo;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.book.domain.entity.Book;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.util.DefaultData;

/**
 * Mapper class to transform a {@link JsonBookVolume} containing book
 * information retrieved from a REST Service to a {@link Book} from the domain.
 */
public class BookRestApiJsonMapper {

    public static final String JSON_DATE_FORMAT = "yyyyMMdd";

    public Book transform(JsonBookVolume jsonBookVolume) {
        if(jsonBookVolume == null) {
            return Book.Companion.getNO_BOOK();
        }

        JsonBookVolumeInfo bookVolumeInfo = jsonBookVolume.getVolumeInfo();
        Book book = new Book(VilibraContract.ENTRY_NOT_SAVED_ID,
                bookVolumeInfo.getTitle(), bookVolumeInfo.getSubtitle(), bookVolumeInfo.getAuthors(),
                bookVolumeInfo.getPublisher(), bookVolumeInfo.getPublishedDate(),
                bookVolumeInfo.getPageCount(), bookVolumeInfo.getIsbn10(), bookVolumeInfo.getIsbn13());

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
