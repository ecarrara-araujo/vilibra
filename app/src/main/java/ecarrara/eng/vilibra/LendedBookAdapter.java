package ecarrara.eng.vilibra;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ecarrara on 20/12/2014.
 */
public class LendedBookAdapter extends CursorAdapter {

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
    }

    public class LendedBookViewHolder {

        public final ImageView mBookPhotoImageView;
        public final TextView mBookNameTextView;
        public final TextView mBookAuthorTextView;

        public LendedBookViewHolder(View itemView) {
            mBookPhotoImageView = (ImageView) itemView.findViewById(R.id.book_photo_image_view);
            mBookNameTextView = (TextView) itemView.findViewById(R.id.book_name_text_view);
            mBookAuthorTextView = (TextView) itemView.findViewById(R.id.book_author_text_view);
        }
    }
}
