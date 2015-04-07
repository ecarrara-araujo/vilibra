package ecarrara.eng.vilibra;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.data.VilibraContract.BookEntry;
import ecarrara.eng.vilibra.data.VilibraContract.LendingEntry;
import ecarrara.eng.vilibra.utils.Utility;
import ecarrara.eng.vilibra.widget.RoundedQuickContactBadge;

/**
 * Created by ecarrara on 23/12/2014.
 */
public class LendedBookDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks <Cursor> {

    private static final String LOG_TAG = LendedBookDetailFragment.class.getSimpleName();
    private static final int BOOK_DETAIL_LOADER = 0;
    private static final int LENDING_DETAIL_LOADER = 1;

    private CursorLoader mBookInfoLoader;
    private Uri mBookUri;
    private Uri mLendingUri;

    private ImageView mBookPhotoImageView;
    private TextView mBookTitleTextView;
    private TextView mBookSubtitleTextView;
    private TextView mBookAuthorsTextView;
    private TextView mBookPublisherTextView;
    private TextView mBookPageCountTextView;
    private TextView mBookISBN10;
    private TextView mBookISBN13;
    private View mLendingContactInfoContainer;
    private RoundedQuickContactBadge mBookLendedContactBadge;
    private TextView mLendingDateTextView;
    private TextView mLendingMessageTextView;
    private Button mLendBookButton;
    private Button mReturnBookButton;

    private static final String VILIBRA_SHARE_HASHTAG = " #ViLibra";
    private ShareActionProvider mShareActionProvider;
    private String mShareMessage;

    private static final String[] BOOKS_DETAIL_COLUMNS = {
            BookEntry.TABLE_NAME + "." + VilibraContract.BookEntry._ID,
            BookEntry.COLUMN_TITLE,
            BookEntry.COLUMN_SUBTITLE,
            BookEntry.COLUMN_AUTHORS,
            BookEntry.COLUMN_PUBLISHER,
            BookEntry.COLUMN_ISBN_10,
            BookEntry.COLUMN_ISBN_13,
            BookEntry.COLUMN_PAGES,
            BookEntry.COLUMN_PUBLISHED_DATE
    };

    public static final int COL_BOOK_ID = 0;
    public static final int COL_BOOK_TITLE = 1;
    public static final int COL_BOOK_SUBTITLE = 2;
    public static final int COL_BOOK_AUTHORS = 3;
    public static final int COL_BOOK_PUBLISHER = 4;
    public static final int COL_BOOK_ISBN_10 = 5;
    public static final int COL_BOOK_ISBN_13 = 6;
    public static final int COL_BOOK_PAGES = 7;
    public static final int COL_BOOK_PUBLISHED_DATE = 8;

    private static final String[] LENDING_DETAIL_COLUMNS = {
            LendingEntry.TABLE_NAME + "." + VilibraContract.LendingEntry._ID,
            LendingEntry.COLUMN_CONTACT_URI,
            LendingEntry.COLUMN_LENDING_DATE
    };

    public static final int COL_LENDING_ID = 0;
    public static final int COL_LENDING_CONTACT = 1;
    public static final int COL_LENDING_DATE = 2;

    public static LendedBookDetailFragment newInstance(Uri bookLendingUri) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI, bookLendingUri);
        LendedBookDetailFragment fragment = new LendedBookDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public LendedBookDetailFragment() {
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lended_book_detail, container, false);

        mBookPhotoImageView = (ImageView) rootView.findViewById(R.id.book_photo_image_view);
        mBookTitleTextView = (TextView) rootView.findViewById(R.id.book_title_text_view);
        mBookSubtitleTextView = (TextView) rootView.findViewById(R.id.book_subtitle_text_view);
        mBookAuthorsTextView = (TextView) rootView.findViewById(R.id.book_authors_text_view);
        mBookPublisherTextView = (TextView) rootView.findViewById(R.id.book_publisher_edition_text_view);
        mBookPageCountTextView = (TextView) rootView.findViewById(R.id.book_page_count_text_view);
        mBookISBN10 = (TextView) rootView.findViewById(R.id.book_isbn10_text_view);
        mBookISBN13 = (TextView) rootView.findViewById(R.id.book_isbn13_text_view);

        mLendingContactInfoContainer = rootView.findViewById(R.id.lending_information_container);
        mBookLendedContactBadge = (RoundedQuickContactBadge)
                mLendingContactInfoContainer.findViewById(R.id.contact_badge);
        mLendingDateTextView = (TextView)
                mLendingContactInfoContainer.findViewById(R.id.book_lending_date);
        mLendingMessageTextView = (TextView) rootView.findViewById(R.id.lending_message_text_view);

        mReturnBookButton = (Button) rootView.findViewById(R.id.return_book_button);
        mReturnBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReturnBookRequest();
            }
        });
        mLendBookButton = (Button) rootView.findViewById(R.id.lend_book_button);
        mLendBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLendBookRequest();
            }
        });

        return rootView;
    }

    private void handleLendBookRequest() {
        Log.d(LOG_TAG, "Book lend requested.");
        proceedToContactAssociation();
    }

    private void handleReturnBookRequest() {
        Log.d(LOG_TAG, "Book return requested.");
        if(mLendingUri != null) {
            int rowsDeleted = getActivity().getContentResolver().delete(mLendingUri, null, null);
            this.getActivity().getSupportFragmentManager().popBackStack();
            Toast.makeText(getActivity(), R.string.book_returned_to_shelf, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null
                && arguments.containsKey(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI)) {
            mBookUri = arguments.getParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI);
            getLoaderManager().initLoader(BOOK_DETAIL_LOADER, null, this);
            getLoaderManager().initLoader(LENDING_DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");

        Uri bookUri =
                getArguments().getParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_URI);
        if(BOOK_DETAIL_LOADER == id) {
            return new CursorLoader(getActivity(), bookUri, BOOKS_DETAIL_COLUMNS, null, null, null);
        } else if (LENDING_DETAIL_LOADER == id) {
            Uri lendingUri =
                    VilibraContract.LendingEntry.buildLendingForABookUri(ContentUris.parseId(bookUri));
            return new CursorLoader(getActivity(), lendingUri, LENDING_DETAIL_COLUMNS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "In onLoadFinished");
        final int operationId = loader.getId();
        if(LENDING_DETAIL_LOADER == operationId) {
            handleLendingDetailData(cursor);
        } else if (BOOK_DETAIL_LOADER == operationId) {
            handleBookDetailData(cursor);
        }
    }

    private void handleLendingDetailData(Cursor cursor) {
        if(!cursor.moveToFirst()) {
            mLendingContactInfoContainer.setVisibility(View.GONE);
            mLendingMessageTextView.setVisibility(View.VISIBLE);
            mLendBookButton.setVisibility(View.VISIBLE);
            mReturnBookButton.setVisibility(View.GONE);
            return;
        }

        String contactUri = cursor.getString(COL_LENDING_CONTACT);
        if(contactUri.length() > 0) {
            mLendingUri = LendingEntry.buildLendingUri(cursor.getLong(COL_LENDING_ID));

            mLendingMessageTextView.setVisibility(View.GONE);
            mLendingContactInfoContainer.setVisibility(View.VISIBLE);
            mLendBookButton.setVisibility(View.GONE);
            mReturnBookButton.setVisibility(View.VISIBLE);

            String lendingDate = cursor.getString(COL_LENDING_DATE);
            mLendingDateTextView.setText(
                    Utility.getFormattedDateForBookInfo(getActivity(), lendingDate));
            Uri contactLookupUri = ContactsContract.Contacts.getLookupUri(
                    getActivity().getContentResolver(),
                    Uri.parse(contactUri)
            );
            mBookLendedContactBadge.assignContactUri(contactLookupUri);
            mBookLendedContactBadge.setImageBitmap(Utility.getThumbnailForContact(getActivity(),
                    contactLookupUri));
        } else {
            mLendingContactInfoContainer.setVisibility(View.GONE);
            mLendingMessageTextView.setVisibility(View.VISIBLE);
            mLendBookButton.setVisibility(View.VISIBLE);
            mReturnBookButton.setVisibility(View.GONE);

        }
    }

    private void handleBookDetailData(Cursor cursor) {
        if(!cursor.moveToFirst()) { return; }

        String bookTitle = cursor.getString(COL_BOOK_TITLE);
        mBookTitleTextView.setText(bookTitle);

        String bookSubtitle = cursor.getString(COL_BOOK_SUBTITLE);
        if(bookSubtitle.length() > 0) {
            mBookSubtitleTextView.setText(bookSubtitle);
        } else {
            mBookSubtitleTextView.setVisibility(View.GONE);
        }

        String bookAuthors = cursor.getString(COL_BOOK_AUTHORS);
        mBookAuthorsTextView.setText(bookAuthors);

        String bookPublisher = cursor.getString(COL_BOOK_PUBLISHER);
        mBookPublisherTextView.setText(bookPublisher);

        int bookPageCount = cursor.getInt(COL_BOOK_PAGES);
        if(bookPageCount > 0) {
            String bookPageString = String.format(getString(R.string.format_book_pages),
                    bookPageCount);
            mBookPageCountTextView.setText(bookPageString);
        } else {
            mBookPageCountTextView.setVisibility(View.GONE);
        }

        String bookIsbn10 = cursor.getString(COL_BOOK_ISBN_10);
        String formattedBookIsbn10 = String.format(getString(R.string.format_isbn_10), bookIsbn10);
        mBookISBN10.setText(formattedBookIsbn10);

        String bookIsbn13 = cursor.getString(COL_BOOK_ISBN_13);
        String formattedBookIsbn13 = String.format(getString(R.string.format_isbn_13), bookIsbn13);
        mBookISBN13.setText(formattedBookIsbn13);

        mShareMessage = String.format(getString(R.string.format_share_message), bookTitle);

        if(mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareLendedBookIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Loader reset was requested...");
    }

    private static final int CONTACT_PICKER_RESULT = 1000;

    private void proceedToContactAssociation() {
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
        getActivity().finish();
    }

    private void handleRetrievedContactUri(Uri contactUri) {
        Log.d(LOG_TAG, contactUri.toString());

        ContentValues contactInfoValues = new ContentValues();
        contactInfoValues.put(LendingEntry.COLUMN_BOOK_KEY,
                BookEntry.getBookIdFromUri(mBookUri));
        contactInfoValues.put(LendingEntry.COLUMN_CONTACT_URI, contactUri.toString());
        contactInfoValues.put(LendingEntry.COLUMN_LENDING_DATE,
                VilibraContract.getDbDateString(new Date()));
        getActivity().getContentResolver().insert(LendingEntry.CONTENT_URI, contactInfoValues);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lended_book_detail_fragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(createShareLendedBookIntent());
    }

    private Intent createShareLendedBookIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareMessage + " " + VILIBRA_SHARE_HASHTAG);
        return shareIntent;
    }
}


