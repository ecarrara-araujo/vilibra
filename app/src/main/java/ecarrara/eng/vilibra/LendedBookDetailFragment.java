package ecarrara.eng.vilibra;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private static final int LENDING_DETAIL_LOADER = 0;

    private CursorLoader mLendingInfoLoader;

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

    private static final String[] LENDED_BOOKS_COLUMNS = {
            BookEntry.TABLE_NAME + "." + VilibraContract.BookEntry._ID,
            BookEntry.COLUMN_TITLE,
            BookEntry.COLUMN_SUBTITLE,
            BookEntry.COLUMN_AUTHORS,
            BookEntry.COLUMN_PUBLISHER,
            BookEntry.COLUMN_ISBN_10,
            BookEntry.COLUMN_ISBN_13,
            BookEntry.COLUMN_PAGES,
            BookEntry.COLUMN_PUBLISHED_DATE,
            LendingEntry.TABLE_NAME + "." + VilibraContract.LendingEntry._ID,
            LendingEntry.COLUMN_CONTACT_URI,
            LendingEntry.COLUMN_LENDING_DATE
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
    public static final int COL_LENDING_ID = 9;
    public static final int COL_LENDING_CONTACT = 10;
    public static final int COL_LENDING_DATE = 11;

    public LendedBookDetailFragment() {

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLoaderManager().restartLoader(LENDING_DETAIL_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        if(arguments != null
                && arguments.containsKey(LendedBookDetailActivity.EXTRA_KEY_BOOK_LENDING_URI)) {
            getLoaderManager().initLoader(LENDING_DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");

        Uri lendingUri =
                getArguments().getParcelable(LendedBookDetailActivity.EXTRA_KEY_BOOK_LENDING_URI);
        return new CursorLoader(getActivity(), lendingUri, LENDED_BOOKS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(LOG_TAG, "In onLoadFinished");

        if(!cursor.moveToFirst()) { return; }

        String bookTitle = cursor.getString(COL_BOOK_TITLE);
        mBookTitleTextView.setText(bookTitle);

        String bookSubtitle = cursor.getString(COL_BOOK_SUBTITLE);
        mBookSubtitleTextView.setText(bookSubtitle);

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

        String contactUri = cursor.getString(COL_LENDING_CONTACT);
        if(contactUri.length() > 0) {
            mLendingMessageTextView.setVisibility(View.GONE);
            mLendingContactInfoContainer.setVisibility(View.VISIBLE);

            String lendingDate = cursor.getString(COL_LENDING_DATE);
            mLendingDateTextView.setText(Utility.getFormattedMonthDay(getActivity(), lendingDate));

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
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
