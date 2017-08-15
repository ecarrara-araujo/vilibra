package br.eng.ecarrara.vilibra.data.mapper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Mapper class for book's authors related information.
 */
public class AuthorsListMapper {

    public static final String AUTHORS_DATABASE_SEPARATOR = ";";
    public static final String AUTHORS_VIEW_SEPARATOR = ",";

    /**
     * Converts a String with a list of authors separated by BookEntry.AUTHORS_DATABASE_SEPARATOR into a
     * Collection of authors String names.
     *
     * @param authors
     * @return {@link List <String>} representing the authors list
     */
    public static List<String> transformAuthorsFromCommaSeparatedList(String authors) {
        String[] authorsArray = authors.split(AUTHORS_DATABASE_SEPARATOR);
        return Arrays.asList(authorsArray);
    }

    /**
     * Converts a collection of authors {@link String} names to a simple {@link String} with the
     * names separated by AUTHORS_DATABASE_SEPARATOR.
     *
     * @param authorsList
     * @return {@link String} with the authors names
     */
    public static String transformAuthorsListToDatabaseFormat(List<String> authorsList) {
        StringBuilder authorsDatabaseFormattedString = new StringBuilder();

        for(Iterator<String> authorsIterator = authorsList.iterator(); authorsIterator.hasNext();) {
            authorsDatabaseFormattedString.append(authorsIterator.next());
            if(authorsIterator.hasNext()) {
                authorsDatabaseFormattedString.append(AUTHORS_DATABASE_SEPARATOR);
            }
        }

        return authorsDatabaseFormattedString.toString();
    }

    /**
     * Converts a collection of authors {@link String} names to a simple {@link String} with the
     * names separated by AUTHORS_VIEW_SEPARATOR.
     *
     * @param authorsList
     * @return {@link String} with the authors names
     */
    public static String transformAuthorsListToViewFormat(List<String> authorsList) {
        StringBuilder authorsViewFormattedString = new StringBuilder();

        for(Iterator<String> authorsIterator = authorsList.iterator(); authorsIterator.hasNext();) {
            authorsViewFormattedString.append(authorsIterator.next());
            if(authorsIterator.hasNext()) {
                authorsViewFormattedString.append(AUTHORS_VIEW_SEPARATOR);
            }
        }

        return authorsViewFormattedString.toString();
    }

}
