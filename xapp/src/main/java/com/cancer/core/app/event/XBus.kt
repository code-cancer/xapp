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

class XBus {

    abstract class Event

    companion object {
        const val TAG = "XBus"
        private val flowsMap = ConcurrentHashMap<String, MutableSharedFlow<Event>>()
        private val mainScope = MainScope()

        fun getFlow(eventClassName: String): MutableSharedFlow<Event> {
            return flowsMap[eventClassName] ?: MutableSharedFlow<Event>().also { flowsMap[eventClassName] = it }
        }

        fun post(event: Event, delay: Long = 0) = mainScope.launchDelay(delay = delay) {
            Log.d(TAG, "post(delay = $delay): $event")
            getFlow(event.javaClass.simpleName).emit(event)
        }

        inline fun <reified T : Event> observe(
            lifecycleOwner: LifecycleOwner,
            dispatcher: CoroutineDispatcher = Dispatchers.Main,
            minState: Lifecycle.State = Lifecycle.State.CREATED,
            crossinline onReceived: (T) -> Unit,
        ) = getFlow(T::class.java.simpleName).collectIn(lifecycleOwner.lifecycleScope, dispatcher) {
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                if (it is T) {
                    Log.d(TAG, "Received: $it")
                    onReceived(it)
                }
            }
        }
    }

}