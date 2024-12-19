package org.riezki.projectmaps.data.utils

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

/**
 * @author riezky maisyar
 */

sealed class NetworkState<out T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    data class Success<T>(val result: T) : NetworkState<T>(data = result)
    data class Error(val throwable: Throwable) : NetworkState<Nothing>(error = throwable)
    data object Loading : NetworkState<Nothing>()
    data object Idle : NetworkState<Nothing>()

    suspend fun onSuccess(data: suspend (T?) -> Unit): NetworkState<T> {
        if (this is Success) data(this.result)
        return this
    }

    suspend fun onError(data: suspend (Throwable) -> Unit): NetworkState<T> {
        if (this is Error) data(this.throwable)
        return this
    }

    suspend fun onLoading(data: suspend () -> Unit): NetworkState<T> {
        if (this is Loading) data()
        return this
    }

    suspend fun onIdle(data: suspend () -> Unit): NetworkState<T> {
        if (this is Idle) data()
        return this
    }
}