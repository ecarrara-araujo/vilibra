package br.eng.ecarrara.vilibra.book.data.datasource.googlebooksrestapi.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class JsonBookVolumeCollection(
        @SerializedName("items") val items: List<JsonBookVolume> = Collections.emptyList()
)

