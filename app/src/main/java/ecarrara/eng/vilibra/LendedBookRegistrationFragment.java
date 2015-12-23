package ecarrara.eng.vilibra;

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
import android.widget.Button;
import android.widget.EditText;

import ecarrara.eng.vilibra.data.VilibraContentValuesBuilder;
import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.model.BookVolume;
import ecarrara.eng.vilibra.service.GoogleBooksService;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookRegistrationFragment extends Fragment
        implements ZBarScannerView.ResultHandler {

    private static final String LOG_TAG = LendedBookRegistrationFragment.class.getSimpleName();

    public interface Callback {
        public void onError(String message);
    }

    private Uri mLendedBookUri;

    private View mMainContentFrame;
    private View mProgressFrame;
    private ZBarScannerView mBarcodeScannerView;
    private EditText mISBNEditText;

    public LendedBookRegistrationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lended_book_registration, container, false);

        mMainContentFrame = rootView.findViewById(R.id.main_content_frame);
        mProgressFrame = rootView.findViewById(R.id.progress_frame);
        mISBNEditText = (EditText) mMainContentFrame.findViewById(R.id.isbn_edit_text);
        mBarcodeScannerView =
                (ZBarScannerView) mMainContentFrame.findViewById(R.id.barcode_scanner_view);


        Button confirmButton = (Button) mMainContentFrame.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRetrieveBookInfo();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        clearLoadingState();

        mBarcodeScannerView.setResultHandler(this);
        mBarcodeScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBarcodeScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        if(mISBNEditText != null) {
            mISBNEditText.setText(result.getContents());
        }
    }

    private void clearLoadingState() {
        mMainContentFrame.setVisibility(View.VISIBLE);
        mProgressFrame.setVisibility(View.GONE);
    }

    private void setLoadingState() {
        mMainContentFrame.setVisibility(View.GONE);
        mProgressFrame.setVisibility(View.VISIBLE);
    }

    private void onRetrieveBookInfo() {
        setLoadingState();
        String isbn = ((EditText) mMainContentFrame
                .findViewById(R.id.isbn_edit_text)).getText().toString();
        FetchBookDataTask fetchBookDataTask = new FetchBookDataTask(getActivity());
        fetchBookDataTask.execute(isbn);
    }

    /**
     * Created by ecarrara on 18/12/2014.
     */
    private class FetchBookDataTask extends AsyncTask<String, Void, Uri> {

        private final String LOG_TAG = FetchBookDataTask.class.getSimpleName();
        private Context mContext;

        public FetchBookDataTask(Context context) {
            mContext = context;
        }

        @Override
        protected Uri doInBackground(String... params) {

            if(params.length == 0) {
                Log.d(LOG_TAG, "ISBN not informed...");
                return null;
            }

            String isbn = params[0];
            Uri bookUri = null;

            Cursor cursor = getActivity().getContentResolver().query(BookEntry.CONTENT_URI, null,
                    BookEntry.COLUMN_ISBN_10 + " = ? OR " + BookEntry.COLUMN_ISBN_13 + " = ?",
                    new String[]{isbn, isbn}, null);
            if(cursor.moveToFirst()) {
                bookUri =
                        BookEntry.buildBookUri(cursor.getLong(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ID)));
            } else {
                GoogleBooksService googleBooksService = new GoogleBooksService();
                BookVolume returnedBookVolume = googleBooksService.lookForVolumeByISBN(isbn);

                if(null != returnedBookVolume) {
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
            mLendedBookUri = uri;
            clearLoadingState();
            if(null != mLendedBookUri) {
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
