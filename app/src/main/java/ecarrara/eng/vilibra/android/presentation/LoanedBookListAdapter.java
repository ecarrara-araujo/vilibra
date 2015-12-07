package ecarrara.eng.vilibra.android.presentation;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ecarrara.eng.vilibra.R;
import ecarrara.eng.vilibra.data.VilibraContract;
import ecarrara.eng.vilibra.domain.presentation.presenter.BorrowedBooksPresenter;
import ecarrara.eng.vilibra.utils.Utility;
import ecarrara.eng.vilibra.widget.RoundedQuickContactBadge;

public class LoanedBookListAdapter
        extends RecyclerView.Adapter<LoanedBookListAdapter.LoanedBookViewHolder> {

    public interface OnItemClickListener {
        void onUserItemClicked(Uri lendingUri);
    }

    public static class LoanedBookViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mBookPhotoImageView;
        public final TextView mBookNameTextView;
        public final TextView mBookAuthorTextView;
        public final RoundedQuickContactBadge mLoanedBookContactBadge;

        public LoanedBookViewHolder(View itemView) {
            super(itemView);
            mBookPhotoImageView = (ImageView) itemView.findViewById(R.id.book_photo_image_view);
            mBookNameTextView = (TextView) itemView.findViewById(R.id.book_name_text_view);
            mBookAuthorTextView = (TextView) itemView.findViewById(R.id.book_author_text_view);
            mLoanedBookContactBadge = (RoundedQuickContactBadge) itemView.findViewById(R.id.contact_badge);
        }
    }

    private static final String LOG_TAG = LoanedBookListAdapter.class.getSimpleName();

    private Context context;
    private Cursor loanedBooksCursor;
    private int currentListPosition;
    private OnItemClickListener onItemClickListener;

    public LoanedBookListAdapter(Context context /*, Cursor loanedBooksCursor*/) {
//        this.validateLoanedBooksCollection(loanedBooksCursor);
        this.context = context;
//        this.loanedBooksCursor = loanedBooksCursor;
    }

    @Override public LoanedBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_lended_book, parent, false);

        LoanedBookViewHolder loanedBookViewHolder = new LoanedBookViewHolder(v);
        return loanedBookViewHolder;
    }

    @Override public void onBindViewHolder(LoanedBookViewHolder loanedBookViewHolder,
                                           final int position) {

        if(this.loanedBooksCursor == null || !this.loanedBooksCursor.moveToPosition(position)) {
            return;
        }

        String bookName = this.loanedBooksCursor.getString(BorrowedBooksPresenter.COL_BOOK_TITLE);
        loanedBookViewHolder.mBookNameTextView.setText(bookName);

        String bookAuthor = this.loanedBooksCursor.getString(BorrowedBooksPresenter.COL_BOOK_AUTHORS);
        loanedBookViewHolder.mBookAuthorTextView.setText(bookAuthor);

        Uri contactLookupUri = ContactsContract.Contacts.getLookupUri(
                context.getContentResolver(),
                Uri.parse(this.loanedBooksCursor.getString(BorrowedBooksPresenter.COL_LENDING_CONTACT))
        );
        loanedBookViewHolder.mLoanedBookContactBadge.assignContactUri(contactLookupUri);
        loanedBookViewHolder.mLoanedBookContactBadge.setImageBitmap(
                Utility.getThumbnailForContact(context, contactLookupUri));

        loanedBookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Log.d(LOG_TAG, "Loaned book item clicked");
                    long lendingId = LoanedBookListAdapter.this.loanedBooksCursor
                            .getLong(BorrowedBooksPresenter.COL_LENDING_ID);
                    long bookId = LoanedBookListAdapter.this.loanedBooksCursor
                            .getLong(BorrowedBooksPresenter.COL_BOOK_ID);

                    Uri lendingUri = VilibraContract.LendingEntry.buildLendingWithBookUri(
                            lendingId, bookId);

                    LoanedBookListAdapter.this.currentListPosition = position;
                    if(LoanedBookListAdapter.this.onItemClickListener != null) {
                        LoanedBookListAdapter.this.onItemClickListener
                                .onUserItemClicked(lendingUri);
                    }
                }
            });
    }

    @Override public int getItemCount() {
        return (this.loanedBooksCursor != null) ? this.loanedBooksCursor.getCount() : 0;
    }

    public void setLoanedBooks(Cursor newLoanedBooksCursor) {
        this.validateLoanedBooksCollection(newLoanedBooksCursor);
        this.loanedBooksCursor = newLoanedBooksCursor;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getCurrentListPosition() {
        if (this.currentListPosition >= getItemCount()) {
            this.currentListPosition= getItemCount() - 1;
        }
        return this.currentListPosition;
    }

    private void validateLoanedBooksCollection(Cursor loanedBooksCursor) {
        if (loanedBooksCursor == null) {
            throw new IllegalArgumentException("The loaned books collection cannot be null");
        }
    }

}
