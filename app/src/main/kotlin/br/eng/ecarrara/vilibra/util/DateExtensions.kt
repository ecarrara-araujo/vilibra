package br.eng.ecarrara.vilibra.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.fromFormattedString(date: String, format: String): Date
        = SimpleDateFormat(format, Locale.getDefault()).parse(date)
