package com.medtek.main.utilties

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val httpCode: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, httpCode: Int? = null): Resource<T>(data, message, httpCode)
//    class Loading<T>(data: T? = null) : Resource<T>(data)
}