package com.kysportsblogs.android.extensions

fun <T> Collection<T>.contains(predicate: (T)->Boolean): Boolean {
    return this.find(predicate) != null
}


fun <T> Collection<T>?.isNotEmpty(): Boolean {
    return (this?.isNotEmpty()) ?: false
}

fun <T> Collection<T>?.isEmpty(): Boolean {
    return (this?.isEmpty()) ?: true
}