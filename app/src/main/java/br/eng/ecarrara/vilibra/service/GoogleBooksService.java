package br.eng.ecarrara.vilibra.service;

import java.io.IOException;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksRestApi;
import br.eng.ecarrara.vilibra.core.networking.ApiConstantsKt;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.model.BookVolumeCollection;
import timber.log.Timber;

public class GoogleBooksService {

    private GoogleBooksRestApi googleBooksRestApi;

    @Inject
    public GoogleBooksService(GoogleBooksRestApi googleBooksRestApi) {
        this.googleBooksRestApi = googleBooksRestApi;
    }

    public BookVolume lookForVolumeByISBN(String isbn) {
        String query = formatQueryForISBNSearch(isbn);
        BookVolumeCollection bookVolumeCollection = lookForVolumesWithQuery(query);
        BookVolume bookVolume = null;
        if(null != bookVolumeCollection) {
            if (bookVolumeCollection.getItems().size() > 0) {
                bookVolume = bookVolumeCollection.getItems().get(0);
            }
        }
        return bookVolume;
    }

    public BookVolumeCollection lookForVolumesWithQuery(String query) {
        BookVolumeCollection bookVolumeCollection = null;
        try {
            bookVolumeCollection = googleBooksRestApi.searchVolumeData(
                    query,
                    ApiConstantsKt.getBOOKS_INFORMATION_SERVICE_API_KEY())
                    .execute()
                    .body();
        } catch (IOException exception) {
            Timber.e(exception);
        }
        return bookVolumeCollection;
    }

    private String formatQueryForISBNSearch(String isbn) {
        final String ISBN_QUERY = "isbn:";
        return ISBN_QUERY + isbn;
    }

}
