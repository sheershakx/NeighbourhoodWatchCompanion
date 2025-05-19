package com.srg.framework.network

import timber.log.Timber
import java.lang.Exception

suspend fun <T : Any> apiCall(call: suspend () -> T): DataState<T> {
    return try {
        val response = call()
        DataState.Success(response)
    } catch (ex: Exception) {
        Timber.tag("ApiCallExtensionError").e(ex)
        DataState.Error(ex)
    }
}