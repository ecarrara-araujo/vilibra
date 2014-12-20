package ecarrara.eng.vilibra;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import ecarrara.eng.vilibra.data.BookInfoRequester;
import ecarrara.eng.vilibra.data.GoogleBooksJsonDataParser;
import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookRegistrationFragment extends Fragment {

    private static final String LOG_TAG = LendedBookRegistrationFragment.class.getSimpleName();

    private Uri mLendedBookUri;

    private View mMainContentFrame;
    private View mProgressFrame;

    public LendedBookRegistrationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lended_book_registration, container, false);

        mMainContentFrame = rootView.findViewById(R.id.main_content_frame);
        mProgressFrame = rootView.findViewById(R.id.progress_frame);

        Button nextButton = (Button) mMainContentFrame.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
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

        Cursor cursor = getActivity().getContentResolver().query(BookEntry.CONTENT_URI, null,
                BookEntry.COLUMN_ISBN_10 + " = ? OR " + BookEntry.COLUMN_ISBN_13 + " = ?",
                new String[]{isbn, isbn}, null);

        if(cursor.moveToFirst()) {
            Intent detailIntent = new Intent(getActivity(), LendedBookDetailActivity.class);
            detailIntent.putExtra(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI,
                    BookEntry.buildBookUri(cursor.getLong(cursor.getColumnIndex(BookEntry._ID))));
            getActivity().startActivity(detailIntent);
        } else {
            FetchBookDataTask fetchBookDataTask = new FetchBookDataTask(getActivity());
            fetchBookDataTask.execute(isbn);
        }
    }

    private static final int CONTACT_PICKER_RESULT = 1000;

    private void onProceedToContactAssociation() {

        Intent contactRequestIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactRequestIntent, CONTACT_PICKER_RESULT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    handleRetrievedContactUri(data.getData());
                    break;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.e(LOG_TAG, "There was an error retrieving activity result.");
        }
    }

    private void handleRetrievedContactUri(Uri contactUri) {
        Log.d(LOG_TAG, contactUri.toString());

        ContentValues contactInfoValues = new ContentValues();
        contactInfoValues.put(LendingEntry.COLUMN_BOOK_KEY,
                BookEntry.getBookIdFromUri(mLendedBookUri));
        contactInfoValues.put(LendingEntry.COLUMN_CONTACT_URI, contactUri.toString());
        contactInfoValues.put(LendingEntry.COLUMN_LENDING_DATE,
                VilibraContract.getDbDateString(new Date()));
        getActivity().getContentResolver().insert(LendingEntry.CONTENT_URI, contactInfoValues);

        getActivity().finish();
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

            BookInfoRequester infoRequester = new BookInfoRequester();
            String bookJsonString = infoRequester.requestBookData(isbn);
            GoogleBooksJsonDataParser parser = new GoogleBooksJsonDataParser();
            ContentValues bookData = parser.parse(bookJsonString);

            Uri insertedBook = null;
            if(null != bookData) {
                insertedBook =
                        mContext.getContentResolver().insert(VilibraContract.BookEntry.CONTENT_URI, bookData);
            }
            return insertedBook;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            mLendedBookUri = uri;
            clearLoadingState();
            if(null != mLendedBookUri) {
                onProceedToContactAssociation();
            }
            //TODO: ser error message to the user here
            super.onPostExecute(uri);
        }
    }

}
