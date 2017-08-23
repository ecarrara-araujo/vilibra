package br.eng.ecarrara.vilibra.fakedata

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.util.DefaultData

object BookFakeDataFactory {

    /**
     * Create a test domain Book with some fake data.
     * "Devs Test Book"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val fakeBookDevsTest by lazy {
        Book(
            title = "Devs Test Book",
            subtitle = "A for devs to use tests to test the code with too much tests.",
            isbn13 = "1234567890123",
            isbn10 = "1234567890",
            pageCount = 434,
            authors = listOf("Crazy Dev Yo", "Man of Code"),
            publishedDate = DefaultData.NOT_INITIALIZED.getString(),
            publisher = "Herbert Richards"
        )
    }

    /**
     * Create a test domain Book with some fake data.
     * "Dominando Android"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val fakeBookDominandoAndroid by lazy {
        Book(
                title = "Dominando o Android",
                subtitle = "Do básico ao avançado",
                isbn13 = "9788575224120",
                isbn10 = "8575224123",
                pageCount = 792,
                authors = listOf("Nelson Glauber"),
                publishedDate = DefaultData.NOT_INITIALIZED.getString(),
                publisher = "Editora Novatec"
        )
    }

    /**
     * Create a test domain Book with some fake data.
     * "Pro Android 4"
     */
    // This is done to make sure that we have a compatible format with the one Vilibra uses.
    val fakeBookProAndroid4 by lazy {
        Book(
                title = "Pro Android 4",
                isbn13 = "9781430239314",
                isbn10 = "143023931X",
                pageCount = 1020,
                authors = listOf("Satya Komatineti", "Dave MacLean"),
                publishedDate = DefaultData.NOT_INITIALIZED.getString(),
                publisher = "Apress"
        )
    }

}
