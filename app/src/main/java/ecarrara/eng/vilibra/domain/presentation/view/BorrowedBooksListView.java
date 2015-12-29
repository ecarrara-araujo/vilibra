package ecarrara.eng.vilibra.domain.presentation.view;

import java.util.List;

import ecarrara.eng.vilibra.domain.entity.BookBorrowing;

/**
 * Interface for implementation of a view that lists Borrowed Books.
 */
public interface BorrowedBooksListView extends LoadDataView {

    void render(List<BookBorrowing> borrowedBooks);

}
