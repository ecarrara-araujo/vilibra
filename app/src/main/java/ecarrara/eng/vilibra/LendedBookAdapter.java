package ecarrara.eng.vilibra;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ecarrara.eng.vilibra.utils.Utility;
import ecarrara.eng.vilibra.widget.RoundedQuickContactBadge;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookAdapter extends CursorAdapter {

    private static final String LOG_TAG = LendedBookAdapter.class.getSimpleName();

    public LendedBookAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_lended_book, parent, false);

        LendedBookViewHolder viewHolder = new LendedBookViewHolder(v);
        v.setTag(viewHolder);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        LendedBookViewHolder lendedBookViewHolder = (LendedBookViewHolder) view.getTag();

        String bookName = mCursor.getString(LendedBookListFragment.COL_BOOK_TITLE);
        lendedBookViewHolder.mBookNameTextView.setText(bookName);

        String bookAuthor = mCursor.getString(LendedBookListFragment.COL_BOOK_AUTHORS);
        lendedBookViewHolder.mBookAuthorTextView.setText(bookAuthor);

        Uri contactLookupUri = ContactsContract.Contacts.getLookupUri(
                mContext.getContentResolver(),
                Uri.parse(mCursor.getString(LendedBookListFragment.COL_LENDING_CONTACT))
        );
        lendedBookViewHolder.mLendedContactBadge.assignContactUri(contactLookupUri);
        lendedBookViewHolder.mLendedContactBadge.setImageBitmap(
                Utility.getThumbnailForContact(mContext, contactLookupUri));

    }



    public class LendedBookViewHolder {

        public final ImageView mBookPhotoImageView;
        public final TextView mBookNameTextView;
        public final TextView mBookAuthorTextView;
        public final RoundedQuickContactBadge mLendedContactBadge;

        public LendedBookViewHolder(View itemView) {
            mBookPhotoImageView = (ImageView) itemView.findViewById(R.id.book_photo_image_view);
            mBookNameTextView = (TextView) itemView.findViewById(R.id.book_name_text_view);
            mBookAuthorTextView = (TextView) itemView.findViewById(R.id.book_author_text_view);
            mLendedContactBadge = (RoundedQuickContactBadge) itemView.findViewById(R.id.contact_badge);
        }
    }
}
