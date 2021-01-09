package com.kysportsblogs.android.data.network

import retrofit2.Response

open class NetworkException(val response: Response<*>, message: String? = null, cause: Throwable? = null): Exception(message, cause)
class NoDataException(response: Response<*>, message: String? = null, cause: Throwable? = null): NetworkException(response, message, cause)