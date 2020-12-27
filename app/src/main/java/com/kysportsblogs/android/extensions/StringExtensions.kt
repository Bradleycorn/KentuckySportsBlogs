package com.kysportsblogs.android.extensions

fun String?.toInt(): Int = when (this) {
    null -> toInt()
    else -> toIntOrNull() ?: 0
}
