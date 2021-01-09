package com.kysportsblogs.android.extensions

import com.dropbox.android.external.store4.StoreResponse

val StoreResponse.Error.exception: Throwable
    get() = when (this) {
        is StoreResponse.Error.Exception -> error
        else -> Exception(this.errorMessageOrNull())
    }