package br.eng.ecarrara.vilibra.book.data.datasource.contentprovider.mapper

import java.util.*

/**
 * Mapper class for book's authors related information.
 */
object AuthorsListMapper {

    val AUTHORS_DATABASE_SEPARATOR = ";"
    val AUTHORS_VIEW_SEPARATOR = ","

    /**
     * Converts a String with a list of authors separated by BookEntry.AUTHORS_DATABASE_SEPARATOR into a
     * Collection of authors String names.
     *
     * @param authors
     * @return [&lt;String&gt;][List] representing the authors list
     */
    fun transformAuthorsFromCommaSeparatedList(authors: String): List<String> {
        val authorsArray = authors.split(AUTHORS_DATABASE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Arrays.asList(*authorsArray)
    }

    /**
     * Converts a collection of authors [String] names to a simple [String] with the
     * names separated by AUTHORS_DATABASE_SEPARATOR.
     *
     * @param authorsList
     * @return [String] with the authors names
     */
    fun transformAuthorsListToDatabaseFormat(authorsList: List<String>): String {
        val authorsDatabaseFormattedString = StringBuilder()

        val authorsIterator = authorsList.iterator()
        while (authorsIterator.hasNext()) {
            authorsDatabaseFormattedString.append(authorsIterator.next())
            if (authorsIterator.hasNext()) {
                authorsDatabaseFormattedString.append(AUTHORS_DATABASE_SEPARATOR)
            }
        }

        return authorsDatabaseFormattedString.toString()
    }

    /**
     * Converts a collection of authors [String] names to a simple [String] with the
     * names separated by AUTHORS_VIEW_SEPARATOR.
     *
     * @param authorsList
     * @return [String] with the authors names
     */
    fun transformAuthorsListToViewFormat(authorsList: List<String>): String {
        val authorsViewFormattedString = StringBuilder()

        val authorsIterator = authorsList.iterator()
        while (authorsIterator.hasNext()) {
            authorsViewFormattedString.append(authorsIterator.next())
            if (authorsIterator.hasNext()) {
                authorsViewFormattedString.append(AUTHORS_VIEW_SEPARATOR)
            }
        }

        return authorsViewFormattedString.toString()
    }

}
