package ecarrara.eng.vilibra.data;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import eng.ecarrara.vilibra.utils.ApisConstants;

/**
 * Created by ecarrara on 10/12/2014.
 */
public class BookInfoRequester {
    private static final String LOG_TAG = BookInfoRequester.class.getSimpleName();
    private static final String GOOGLE_BOOKS_API_KEY = ApisConstants.GOOGLE_BOOKS_API_KEY;

    public String requestBookData(String bookIsbn) {

        String bookJsonString = "";

        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the Google Books Query query
            // API description at https://developers.google.com/books/docs/v1/reference/
            final String BASE_URL =
                    "https://www.googleapis.com/books/v1/volumes?";
            final String QUERY_PARAM = "q";
            final String QUERY_PARAM_ISBN = "isbn:" + bookIsbn;
            final String API_KEY_PARAM = "key";


            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, QUERY_PARAM_ISBN)
                    //.appendQueryParameter(API_KEY_PARAM, GOOGLE_BOOKS_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            // Tried to add the API Key as a query parameter but the Google Books Web Service
            // continuously returns 403 error. By recommendation changed to HTTP header.
            // Check: http://stackoverflow.com/questions/20825036/google-books-api-for-android-access-not-configured
            // TODO: Analyze this issue further
            urlConnection.setRequestProperty(API_KEY_PARAM, GOOGLE_BOOKS_API_KEY);
            urlConnection.connect();

            boolean isError = urlConnection.getResponseCode() >= 400;
            InputStream inputStream = null;
            if(isError) {
                inputStream = urlConnection.getErrorStream();
            } else {
                inputStream = urlConnection.getInputStream();
            }

            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                //adding a new line to make the debug process easier.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            bookJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return bookJsonString;
    }

}
