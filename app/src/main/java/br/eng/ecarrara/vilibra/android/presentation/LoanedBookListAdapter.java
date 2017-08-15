package br.eng.ecarrara.vilibra.android.presentation;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.eng.ecarrara.vilibra.R;
import br.eng.ecarrara.vilibra.data.VilibraContract;
import br.eng.ecarrara.vilibra.data.mapper.AuthorsListMapper;
import br.eng.ecarrara.vilibra.domain.entity.Book;
import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;
import br.eng.ecarrara.vilibra.utils.Utility;
import br.eng.ecarrara.vilibra.widget.RoundedQuickContactBadge;

public class LoanedBookListAdapter
        extends RecyclerView.Adapter<LoanedBookListAdapter.LoanedBookViewHolder> {

    public interface OnItemClickListener {
        void onUserItemClicked(Uri bookBorrowingUri);
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
    private List<BookBorrowing> bookBorrowingList;
    private int currentListPosition;
    private OnItemClickListener onItemClickListener;

    public LoanedBookListAdapter(Context context) {
        this.context = context;
        this.bookBorrowingList = new ArrayList<>();
    }

    @Override public LoanedBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_lended_book, parent, false);

        LoanedBookViewHolder loanedBookViewHolder = new LoanedBookViewHolder(v);
        return loanedBookViewHolder;
    }

    @Override public void onBindViewHolder(LoanedBookViewHolder loanedBookViewHolder,
                                           final int position) {

        if(this.bookBorrowingList.size() < position) {
            return;
        }

        final BookBorrowing bookBorrowing = this.bookBorrowingList.get(position);
        final Book book = bookBorrowing.getBorrowedBook();

        loanedBookViewHolder.mBookNameTextView.setText(book.getTitle());
        loanedBookViewHolder.mBookAuthorTextView.setText(
                AuthorsListMapper.transformAuthorsListToViewFormat(book.getAuthors()));

        // TODO: Take the whole contact things look up from here
        Uri contactLookupUri = ContactsContract.Contacts.getLookupUri(
                context.getContentResolver(),
                Uri.parse(bookBorrowing.getBorrower().getContactInformationUri())
        );
        loanedBookViewHolder.mLoanedBookContactBadge.assignContactUri(contactLookupUri);
        loanedBookViewHolder.mLoanedBookContactBadge.setImageBitmap(
                Utility.getThumbnailForContact(context, contactLookupUri));

        loanedBookViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Log.d(LOG_TAG, "Borrowed book item clicked");
                long lendingId = bookBorrowing.getId();
                long bookId = book.getId();

                Uri lendingUri = VilibraContract.LendingEntry.buildLendingWithBookUri(
                        lendingId, bookId);
                LoanedBookListAdapter.this.currentListPosition = position;
                if (LoanedBookListAdapter.this.onItemClickListener != null) {
                    LoanedBookListAdapter.this.onItemClickListener
                            .onUserItemClicked(lendingUri);
                }
            }
        });
    }

    @Override public int getItemCount() {
        return (this.bookBorrowingList != null) ? this.bookBorrowingList.size() : 0;
    }

    public void setLoanedBooks(List<BookBorrowing> newBookBorrowingList) {
        this.validateLoanedBooksCollection(newBookBorrowingList);
        this.bookBorrowingList = newBookBorrowingList;
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

    private void validateLoanedBooksCollection(List<BookBorrowing> bookBorrowingList) {
        if (bookBorrowingList == null) {
            throw new IllegalArgumentException("The loaned books collection cannot be null");
        }
    }

}
