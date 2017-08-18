package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import br.eng.ecarrara.vilibra.util.DefaultData
import br.eng.ecarrara.vilibra.util.fromFormattedString
import com.google.gson.annotations.SerializedName
import java.util.*

data class JsonBookVolume(
        @SerializedName("id") val id: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("volumeInfo") val volumeInfo: JsonBookVolumeInfo? = null
) {
    fun toBook() = Book(
            id = DefaultData.NOT_INITIALIZED.getLong(),
            title = volumeInfo?.title ?: DefaultData.NOT_INITIALIZED.getString(),
            subtitle = volumeInfo?.subtitle ?: DefaultData.NOT_INITIALIZED.getString(),
            authors = volumeInfo?.authors ?: Collections.emptyList(),
            publisher = volumeInfo?.publisher ?: DefaultData.NOT_INITIALIZED.getString(),
            publishedDate = volumeInfo?.publishedDate ?: DefaultData.NOT_INITIALIZED.getDate(),
            pageCount = volumeInfo?.pageCount ?: 0,
            isbn10 = volumeInfo?.isbn10 ?: DefaultData.NOT_INITIALIZED.getString(),
            isbn13 = volumeInfo?.isbn13 ?: DefaultData.NOT_INITIALIZED.getString()
    )
}

data class JsonBookVolumeInfo(
        @SerializedName("title") val title: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("subtitle") val subtitle: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("authors") val authors: List<String> = Collections.emptyList(),
        @SerializedName("publisher") val publisher: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("publishedDate") val publishedDateString: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("pageCount") val pageCount: Int = 0,
        @SerializedName("industryIdentifiers") val industryIdentifiers: List<JsonIndustryIdentifier> = Collections.emptyList()
) {
    companion object {
        val JSON_DATE_FORMAT = "yyyy-MM-dd"
    }

    val authorsAsSemicolonsSeparatedList: String
        get() = this.authors.joinToString(";")

    val publishedDate: Date
        get() = Date().fromFormattedString(publishedDateString, JSON_DATE_FORMAT)

    val isbn10: String
        get() = getIndustryIdentifierByType(JsonIndustryIdentifier.BOOK_ISBN_TYPE_10)

    val isbn13: String
        get() = getIndustryIdentifierByType(JsonIndustryIdentifier.BOOK_ISBN_TYPE_13)

    private fun getIndustryIdentifierByType(type: String): String {
        if (type.isEmpty() || industryIdentifiers.isEmpty()) {
            return DefaultData.NOT_INITIALIZED.getString()
        }
        return industryIdentifiers.find { (identifierType) -> identifierType == type }?.
                identifier ?: DefaultData.NOT_INITIALIZED.getString()
    }
}

data class JsonIndustryIdentifier(
        @SerializedName("type") val type: String = DefaultData.NOT_INITIALIZED.getString(),
        @SerializedName("identifier") val identifier: String = DefaultData.NOT_INITIALIZED.getString()
) {
    companion object {
        val BOOK_ISBN_TYPE_10 = "ISBN_10"
        val BOOK_ISBN_TYPE_13 = "ISBN_13"
    }
}
