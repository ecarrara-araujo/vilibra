package br.eng.ecarrara.vilibra.fixture

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract.getDateFromDb
import br.eng.ecarrara.vilibra.core.data.datasource.contentprovider.VilibraContract.getDbDateString
import java.util.*

object BookFixture {

    /**
     * Create a test domain Book with some fake data.
     * "Devs Test Book"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val testBookDevsTestBook: Book
        get() {
            val publishingDate = getDateFromDb(getDbDateString(Calendar.getInstance().time))

            return Book(
                    id = 1L,
                    title = "Devs Test Book",
                    subtitle = "A for devs to use tests to test the code with too much tests.",
                    isbn13 = "1234567890123",
                    isbn10 = "1234567890",
                    pageCount = 434,
                    authors = Arrays.asList("Crazy Dev Yo", "Man of Code"),
                    publishedDate = publishingDate,
                    publisher = "Herbert Richards"
            )
        }

    /**
     * Create a test domain Book with some fake data.
     * "Dominando Android"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val testBookDominandoAndroid: Book
        get() {
            val publishingDate = getDateFromDb("20140101")

            return Book(
                    id = 1L,
                    title = "Dominando o Android",
                    subtitle = "Do básico ao avançado",
                    isbn13 = "9788575224120",
                    isbn10 = "8575224123",
                    pageCount = 792,
                    authors = Arrays.asList("Nelson Glauber"),
                    publishedDate = publishingDate,
                    publisher = "Editora Novatec"
            )

        }

    /**
     * Create a test domain Book with some fake data.
     * "Pro Android 4"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val testBookProAndroid4: Book
        get() {
            val publishingDate = getDateFromDb("20120101")

            return Book(
                    id = 1L,
                    title = "Pro Android 4",
                    isbn13 = "9781430239314",
                    isbn10 = "143023931X",
                    pageCount = 1020,
                    authors = Arrays.asList("Satya Komatineti", "Dave MacLean"),
                    publishedDate = publishingDate,
                    publisher = "Apress"
            )

        }

}
