package br.eng.ecarrara.vilibra.domain.presentation.view;

import java.util.List;

import br.eng.ecarrara.vilibra.domain.entity.BookBorrowing;

/**
 * Interface for implementation of a view that lists Borrowed Books.
 */
public interface BorrowedBooksListView extends LoadDataView {

    void render(List<BookBorrowing> borrowedBooks);

}
