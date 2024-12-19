package org.riezki.projectmaps.data.base

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import org.riezki.projectmaps.data.network.NetworkClient
import org.riezki.projectmaps.data.utils.NetworkState

/**
 * @author riezky maisyar
 */

abstract class BaseRepository(
    protected val networkClient: NetworkClient = NetworkClient()
) {

    suspend fun getHttpResponse(url: String) : HttpResponse {
        return networkClient.httpClient.get(url)
    }

    protected inline fun <reified T, U>(suspend () -> HttpResponse).reduce(
        crossinline block: (T) -> NetworkState<U>
    ) : Flow<NetworkState<U>> {
        return flow {
            val httpResponse = invoke()
            if (httpResponse.status.isSuccess()) {
                val data = httpResponse.body<T>()
                emit(block.invoke(data))
            } else {
                val throwable = Throwable(httpResponse.bodyAsText())
                emit(NetworkState.Error(throwable))
            }
        }.onStart {
            emit(NetworkState.Loading)
        }.catch {
            emit(NetworkState.Error(Throwable(it.message)))
        }
    }
}