package br.eng.ecarrara.vilibra.data;

import android.content.ContentValues;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract;

public class VilibraContentValuesBuilder {

    public static ContentValues buildFor(JsonBookVolume jsonBookVolume) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_10,
                jsonBookVolume.getVolumeInfo().getIsbn10());
        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_13,
                jsonBookVolume.getVolumeInfo().getIsbn13());
        contentValues.put(VilibraContract.BookEntry.COLUMN_TITLE,
                jsonBookVolume.getVolumeInfo().getTitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_SUBTITLE,
                jsonBookVolume.getVolumeInfo().getSubtitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_AUTHORS,
                jsonBookVolume.getVolumeInfo().getAuthorsAsSemicolonsSeparatedList());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHER,
                jsonBookVolume.getVolumeInfo().getPublisher());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHED_DATE,
                getDbDateString(jsonBookVolume.getVolumeInfo().getPublishedDateString()));
        contentValues.put(VilibraContract.BookEntry.COLUMN_PAGES,
                jsonBookVolume.getVolumeInfo().getPageCount());

        return contentValues;
    }

    private static String getDbDateString(String bookVolumeDate) {
        return bookVolumeDate.replaceAll("-", "");
    }

}
