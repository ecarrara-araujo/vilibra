package br.eng.ecarrara.vilibra.service;

import java.io.IOException;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.GoogleBooksRestApi;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolumeCollection;
import br.eng.ecarrara.vilibra.core.networking.ApiConstantsKt;
import timber.log.Timber;

public class GoogleBooksService {

    private GoogleBooksRestApi googleBooksRestApi;

    @Inject
    public GoogleBooksService(GoogleBooksRestApi googleBooksRestApi) {
        this.googleBooksRestApi = googleBooksRestApi;
    }

    public JsonBookVolume lookForVolumeByISBN(String isbn) {
        String query = formatQueryForISBNSearch(isbn);
        JsonBookVolumeCollection bookVolumeCollection = lookForVolumesWithQuery(query);
        JsonBookVolume bookVolume = null;
        if(null != bookVolumeCollection) {
            if (bookVolumeCollection.getItems().size() > 0) {
                bookVolume = bookVolumeCollection.getItems().get(0);
            }
        }
        return bookVolume;
    }

    public JsonBookVolumeCollection lookForVolumesWithQuery(String query) {
        JsonBookVolumeCollection bookVolumeCollection = null;
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
