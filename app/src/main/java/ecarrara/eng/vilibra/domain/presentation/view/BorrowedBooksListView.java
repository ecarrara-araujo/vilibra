package ecarrara.eng.vilibra.domain.presentation.view;

import android.database.Cursor;

import java.util.List;

import ecarrara.eng.vilibra.model.BookVolume;

/**
 * Interface for implementation of a view that lists Borrowed Books.
 */
public interface BorrowedBooksListView extends LoadDataView {

    void render(Cursor borrowedBooks);

}
