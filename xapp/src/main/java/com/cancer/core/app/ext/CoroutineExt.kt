package com.cancer.core.app.ext

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchDelay(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    delay: Long,
    block: suspend CoroutineScope.() -> Unit,
): Job {
    return launch(context, start) {
        delay(delay)
        block()
    }
}

inline fun <T> Flow<T>.collectIn(
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    crossinline block: suspend (value: T) -> Unit,
) = coroutineScope.launch(dispatcher) {
    collect { block(it) }
}