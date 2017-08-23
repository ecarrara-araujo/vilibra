package br.eng.ecarrara.vilibra.util

import br.eng.ecarrara.vilibra.book.domain.entity.Book
import java.util.*

enum class DefaultData {

    NOT_INITIALIZED;

    fun getString() = DEFAULT_STRING
    fun getInt() = DEFAULT_INT
    fun getDate() = DEFAULT_DATE
    fun getLong() = DEFAULT_LONG

    companion object {
        private val DEFAULT_STRING = ""
        private val DEFAULT_INT = -33
        private val DEFAULT_LONG = -33L
        private val DEFAULT_DATE: Date

        fun isInvalid(data: Any): Boolean {
            when (data) {
                is String -> return data == DEFAULT_STRING
                is Int -> return data == DEFAULT_INT
                is Date -> return data == DEFAULT_DATE
                else -> return false
            }
        }

        init {
            val calendar = Calendar.getInstance()
            calendar.set(1, 1, 1, 0, 0, 0)
            DEFAULT_DATE = calendar.time
        }
    }

}