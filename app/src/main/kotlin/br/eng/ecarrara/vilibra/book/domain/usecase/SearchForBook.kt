package br.eng.ecarrara.vilibra.book.domain.usecase

import br.eng.ecarrara.vilibra.book.domain.BookRepository
import javax.inject.Inject

class SearchForBook
@Inject constructor(private val bookRepository: BookRepository) {
    fun execute(bookIsbn: String) = bookRepository.getByIsbn(bookIsbn)
}