package br.eng.ecarrara.vilibra;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import javax.inject.Inject;

import br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model.JsonBookVolume;
import br.eng.ecarrara.vilibra.core.di.VilibraInjector;
import br.eng.ecarrara.vilibra.data.VilibraContentValuesBuilder;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.data.VilibraContract.BookEntry;
import br.eng.ecarrara.vilibra.service.GoogleBooksService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LendedBookRegistrationFragment extends Fragment {

    @Inject
    GoogleBooksService googleBooksService;

    @BindView(R.id.main_content_frame)
    View mainContentFrame;

    @BindView(R.id.progress_frame)
    View progressFrame;

    @BindView(R.id.barcode_scanner_view)
    DecoratedBarcodeView barcodeScannerView;

    @BindView(R.id.isbn_edit_text)
    EditText isbnEditText;

    private String lastBarcodeContentRead;
    private Uri borrowedBookUri;

    public interface Callback {
        public void onError(String message);
    }

    private BarcodeCallback barcodeReadingCallback = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastBarcodeContentRead)) {
                return;
            }

            lastBarcodeContentRead = result.getText();
            isbnEditText.setText(result.getText());
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }

    };

    public LendedBookRegistrationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lended_book_registration, container, false);
        VilibraInjector.INSTANCE.getVilibraComponent().inject(this);
        ButterKnife.bind(this, rootView);

        barcodeScannerView.decodeContinuous(barcodeReadingCallback);

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        clearLoadingState();
        barcodeScannerView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }

    @OnClick(R.id.confirm_button)
    public void confirm(View view) {
        onRetrieveBookInfo();
    }

    private void clearLoadingState() {
        mainContentFrame.setVisibility(View.VISIBLE);
        progressFrame.setVisibility(View.GONE);
    }

    private void setLoadingState() {
        mainContentFrame.setVisibility(View.GONE);
        progressFrame.setVisibility(View.VISIBLE);
    }

    private void onRetrieveBookInfo() {
        setLoadingState();
        String isbn = ((EditText) mainContentFrame
                .findViewById(R.id.isbn_edit_text)).getText().toString();
        FetchBookDataTask fetchBookDataTask = new FetchBookDataTask(getActivity());
        fetchBookDataTask.execute(isbn);
    }

    private class FetchBookDataTask extends AsyncTask<String, Void, Uri> {

        private final String LOG_TAG = FetchBookDataTask.class.getSimpleName();
        private Context mContext;

        public FetchBookDataTask(Context context) {
            mContext = context;
        }

        @Override
        protected Uri doInBackground(String... params) {

            if (params.length == 0) {
                Log.d(LOG_TAG, "ISBN not informed...");
                return null;
            }

            String isbn = params[0];
            Uri bookUri = null;

            Cursor cursor = getActivity().getContentResolver().query(BookEntry.CONTENT_URI, null,
                    BookEntry.COLUMN_ISBN_10 + " = ? OR " + BookEntry.COLUMN_ISBN_13 + " = ?",
                    new String[]{isbn, isbn}, null);
            if (cursor.moveToFirst()) {
                bookUri =
                        BookEntry.buildBookUri(cursor.getLong(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ID)));
            } else {
                JsonBookVolume returnedBookVolume = googleBooksService.lookForVolumeByISBN(isbn);

                if (null != returnedBookVolume) {
                    ContentValues bookData = VilibraContentValuesBuilder
                            .buildFor(returnedBookVolume);
                    bookUri = mContext.getContentResolver()
                            .insert(VilibraContract.BookEntry.CONTENT_URI, bookData);
                }
            }
            return bookUri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            borrowedBookUri = uri;
            clearLoadingState();
            if (null != borrowedBookUri) {
                Intent detailIntent = new Intent(getActivity(), LendedBookDetailActivity.class);
                detailIntent.putExtra(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI, uri);
                getActivity().startActivity(detailIntent);
                getActivity().finish();
            } else {
                ((Callback) getActivity()).onError(getString(R.string.error_book_data_retrieve));
            }
            super.onPostExecute(uri);
        }
    }

}
