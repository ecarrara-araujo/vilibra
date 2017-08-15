package br.eng.ecarrara.vilibra.book.domain.entity

import br.eng.ecarrara.vilibra.util.DefaultData
import java.util.*

data class Book(
        val id: Long = DefaultData.NOT_INITIALIZED.getLong(),
        val title: String = DefaultData.NOT_INITIALIZED.getString(),
        val subtitle: String = DefaultData.NOT_INITIALIZED.getString(),
        val authors: List<String> = Collections.emptyList(),
        val publisher: String = DefaultData.NOT_INITIALIZED.getString(),
        val publishedDate: Date = DefaultData.NOT_INITIALIZED.getDate(),
        val pageCount: Int = DefaultData.NOT_INITIALIZED.getInt(),
        val isbn10: String = DefaultData.NOT_INITIALIZED.getString(),
        val isbn13: String = DefaultData.NOT_INITIALIZED.getString()
) {
    companion object {
        val NO_BOOK = Book()
    }
}
