package com.kysportsblogs.android.util.extensions

import java.text.SimpleDateFormat
import java.util.*

val DATE_FORMAT_MDY
    get() = "M/dd/yyyy"

val DATE_FORMAT_TIME
    get() = "h:mm a"

val DATE_FORMAT_DAY_OF_MONTH
    get() = "MMM d"

fun Long.toDateString(format: String = DATE_FORMAT_MDY) = Date(this).toString(format)

fun Date.toString(format: String = DATE_FORMAT_MDY, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun Date.isSameDay(other: Date): Boolean {
    return toString(DATE_FORMAT_MDY) == other.toString(DATE_FORMAT_MDY)
}

val Date?.isToday: Boolean
    get() = when (this) {
        null -> false
        else -> isSameDay(Date())
    }

val Date?.isYesterday: Boolean
    get() = when (this) {
        null -> false
        else -> {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DATE, -1)
            val yesterday = cal.time
            isSameDay(yesterday)
        }
    }


fun String.toDate(format: String, isGMT: Boolean = false, locale: Locale = Locale.US): Date? {
    val formatter = SimpleDateFormat(format, locale)
    formatter.timeZone = TimeZone.getTimeZone("GMT")
    return formatter.parse(this)
}

