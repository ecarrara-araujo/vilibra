package ecarrara.eng.vilibra.service;

import android.widget.Toast;

import ecarrara.eng.vilibra.model.BookVolume;
import ecarrara.eng.vilibra.model.BookVolumeCollection;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static ecarrara.eng.vilibra.utils.ApisConstants.GOOGLE_BOOKS_API_KEY;
import static ecarrara.eng.vilibra.utils.ApisConstants.GOOGLE_BOOKS_BASE_URL;

public class GoogleBooksService {

    private RestAdapter mRestAdapter;
    private GoogleBooksServiceInterface mGoogleBooksServiceInterface;
    private GoogleBooksServiceCallback mGoogleBooksServiceCallback;

    public GoogleBooksService() {

        mRestAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(GOOGLE_BOOKS_BASE_URL)
                .build();

        mGoogleBooksServiceInterface = mRestAdapter.create(GoogleBooksServiceInterface.class);
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
        return mGoogleBooksServiceInterface.searchVolumeData(query, GOOGLE_BOOKS_API_KEY);
    }

    private String formatQueryForISBNSearch(String isbn) {
        final String ISBN_QUERY = "isbn:";
        return ISBN_QUERY + isbn;
    }

}
