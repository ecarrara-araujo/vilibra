package br.eng.ecarrara.vilibra.fixture;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import br.eng.ecarrara.vilibra.domain.entity.Book;

import static br.eng.ecarrara.vilibra.data.VilibraContract.getDateFromDb;
import static br.eng.ecarrara.vilibra.data.VilibraContract.getDbDateString;

public class BookFixture {

    /**
     * Create a test domain Book with some fake data.
     * "Devs Test Book"
     */
    public static Book getTestBookDevsTestBook() {
        // This is done to make sure that we have a compatible format with the one Vilibra uses.
        Date publishingDate = getDateFromDb(getDbDateString(Calendar.getInstance().getTime()));

        return new Book.Builder(1L, "Devs Test Book")
                .setSubtitle("A for devs to use tests to test the code with too much tests.")
                .setIsbn13("1234567890123")
                .setIsbn10("1234567890")
                .setPageCount(434)
                .setAuthors(Arrays.asList("Crazy Dev Yo", "Man of Code"))
                .setPublishedDate(publishingDate)
                .setPublisher("Herbert Richards")
                .build();
    }

    /**
     * Create a test domain Book with some fake data.
     * "Dominando Android"
     */
    public static Book getTestBookDominandoAndroid() {
        // This is done to make sure that we have a compatible format with the one Vilibra uses.
        Date publishingDate = getDateFromDb("20140101");

        return new Book.Builder(1L, "Dominando o Android")
                .setSubtitle("Do básico ao avançado")
                .setIsbn13("9788575224120")
                .setIsbn10("8575224123")
                .setPageCount(792)
                .setAuthors(Arrays.asList("Nelson Glauber"))
                .setPublishedDate(publishingDate)
                .setPublisher("Editora Novatec")
                .build();
    }

    /**
     * Create a test domain Book with some fake data.
     * "Pro Android 4"
     */
    public static Book getTestBookProAndroid4() {
        // This is done to make sure that we have a compatible format with the one Vilibra uses.
        Date publishingDate = getDateFromDb("20120101");

        return new Book.Builder(1L, "Pro Android 4")
                .setSubtitle("")
                .setIsbn13("9781430239314")
                .setIsbn10("143023931X")
                .setPageCount(1020)
                .setAuthors(Arrays.asList("Satya Komatineti", "Dave MacLean"))
                .setPublishedDate(publishingDate)
                .setPublisher("Apress")
                .build();
    }


}
