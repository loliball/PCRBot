package utils

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