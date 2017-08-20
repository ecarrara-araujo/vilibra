package br.eng.ecarrara.vilibra.data;

import android.content.ContentValues;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;

public class VilibraContentValuesBuilder {

    public static ContentValues buildFor(JsonBookVolume bookVolume) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_10,
                bookVolume.getVolumeInfo().getIsbn10());
        contentValues.put(VilibraContract.BookEntry.COLUMN_ISBN_13,
                bookVolume.getVolumeInfo().getIsbn13());
        contentValues.put(VilibraContract.BookEntry.COLUMN_TITLE,
                bookVolume.getVolumeInfo().getTitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_SUBTITLE,
                bookVolume.getVolumeInfo().getSubtitle());
        contentValues.put(VilibraContract.BookEntry.COLUMN_AUTHORS,
                bookVolume.getVolumeInfo().getAuthorsAsSemicolonsSeparatedList());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHER,
                bookVolume.getVolumeInfo().getPublisher());
        contentValues.put(VilibraContract.BookEntry.COLUMN_PUBLISHED_DATE,
                getDbDateString(bookVolume.getVolumeInfo().getPublishedDateString()));
        contentValues.put(VilibraContract.BookEntry.COLUMN_PAGES,
                bookVolume.getVolumeInfo().getPageCount());

        return contentValues;
    }

    private static String getDbDateString(String bookVolumeDate) {
        return bookVolumeDate.replaceAll("-", "");
    }

}
