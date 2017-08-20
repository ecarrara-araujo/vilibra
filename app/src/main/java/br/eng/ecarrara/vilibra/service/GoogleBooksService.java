package br.eng.ecarrara.vilibra.service;

import android.widget.Toast;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.core.networking.ApiConstantsKt;
import br.eng.ecarrara.vilibra.model.BookVolume;
import br.eng.ecarrara.vilibra.model.BookVolumeCollection;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoogleBooksService {

    private GoogleBooksServiceInterface mGoogleBooksServiceInterface;
    private GoogleBooksServiceCallback mGoogleBooksServiceCallback;

    @Inject
    public GoogleBooksService(GoogleBooksServiceInterface googleBooksServiceInterface) {
        mGoogleBooksServiceInterface = googleBooksServiceInterface;
        mGoogleBooksServiceCallback = new GoogleBooksServiceCallback();

    }

    private class GoogleBooksServiceCallback implements Callback<BookVolumeCollection> {
        BookVolumeCollection bookVolumeCollection;

        @Override
        public void success(BookVolumeCollection bookVolumeCollection, Response response) {
            this.bookVolumeCollection = bookVolumeCollection;
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(null, error.getMessage(), Toast.LENGTH_LONG).show();
        }
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
        return mGoogleBooksServiceInterface.searchVolumeData(query, ApiConstantsKt.getBOOKS_INFORMATION_SERVICE_API_KEY());
    }

    private String formatQueryForISBNSearch(String isbn) {
        final String ISBN_QUERY = "isbn:";
        return ISBN_QUERY + isbn;
    }

}
