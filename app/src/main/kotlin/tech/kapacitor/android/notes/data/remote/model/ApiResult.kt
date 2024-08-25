package tech.kapacitor.android.notes.data.remote.model

import tech.kapacitor.android.notes.data.remote.error.APIErrorReason

sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()

    data class Error(val message: String, val reason: APIErrorReason) : ApiResult<Nothing>()

    data class Fatal(val throwable: Throwable) : ApiResult<Nothing>()
}