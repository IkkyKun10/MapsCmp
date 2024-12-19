package org.riezki.projectmaps.data.utils

import androidx.compose.runtime.Composable
import org.riezki.projectmaps.data.utils.NetworkState.Error
import org.riezki.projectmaps.data.utils.NetworkState.Idle
import org.riezki.projectmaps.data.utils.NetworkState.Loading
import org.riezki.projectmaps.data.utils.NetworkState.Success

/**
 * @author riezky maisyar
 */

@Composable
fun <T> NetworkState<T>.onSuccess(block: @Composable (T?) -> Unit) {
    when (val state = this) {
        is Success -> block.invoke(state.result)
        else -> Unit
    }
}

@Composable
fun <T> NetworkState<T>.onError(block: @Composable (Throwable) -> Unit) {
    when (val state = this) {
        is Error -> block.invoke(state.throwable)
        else -> Unit
    }
}

@Composable
fun <T> NetworkState<T>.onLoading(block: @Composable () -> Unit) {
    when (this) {
        is Loading -> block.invoke()
        else -> Unit
    }
}

@Composable
fun <T> NetworkState<T>.onIdle(block: @Composable () -> Unit) {
    when (this) {
        is Idle -> block.invoke()
        else -> Unit
    }
}