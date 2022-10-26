package utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

val Int.xxxx get() = DecimalFormat("####,####").format(this)
val Long.xxxx get() = DecimalFormat("####,####").format(this)
val LOCAL_DATE_TIME: DateTimeFormatter = DateTimeFormatterBuilder()
    .parseCaseInsensitive()
    .append(DateTimeFormatter.ISO_LOCAL_DATE)
    .appendLiteral(' ')
    .append(DateTimeFormatter.ISO_LOCAL_TIME)
    .toFormatter()

fun <T> T?.log() = apply { println(this) }

inline fun <reified T : Any> T.toJson() = Json.encodeToString(this)

inline fun <reified T> String.fromJson() = Json.decodeFromString<T>(this)
