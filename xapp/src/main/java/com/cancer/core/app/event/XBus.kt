package com.cancer.core.app.event

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import com.cancer.core.app.ext.collectIn
import com.cancer.core.app.ext.launchDelay
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

object XBus {

    private const val TAG = "XBus"
    private val flowsMap = ConcurrentHashMap<String, MutableSharedFlow<IEvent>>()
    private val mainScope = MainScope()

    fun getFlow(eventClassName: String): MutableSharedFlow<IEvent> {
        return flowsMap[eventClassName] ?: MutableSharedFlow<IEvent>().also { flowsMap[eventClassName] = it }
    }

    fun post(event: IEvent, delay: Long = 0) = mainScope.launchDelay(delay = delay) {
        Log.d(TAG, "post(delay = $delay): $event")
        getFlow(event.javaClass.simpleName).emit(event)
    }

    inline fun <reified T : IEvent> observe(
        lifecycleOwner: LifecycleOwner,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        crossinline onReceived: (T) -> Unit
    ) = getFlow(T::class.java.simpleName).collectIn(lifecycleOwner.lifecycleScope, dispatcher) {
        lifecycleOwner.lifecycle.whenStateAtLeast(minState) { if (it is T) onReceived(it) }
    }

}