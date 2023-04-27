package com.cancer.core.app.event

import android.util.Log
import androidx.lifecycle.*
import com.cancer.core.app.XApp
import com.cancer.core.app.ext.collectIn
import com.cancer.core.app.ext.launchDelay
import com.cancer.core.app.log.XLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

object XBus {

    const val TAG = "XBusLog"
    private val flowsMap = ConcurrentHashMap<String, MutableSharedFlow<Event>>()

    fun getFlow(eventClassName: String): MutableSharedFlow<Event> {
        return flowsMap[eventClassName] ?: MutableSharedFlow<Event>().also { flowsMap[eventClassName] = it }
    }

    fun post(event: Event, delay: Long = 0) = XApp.appScope.launchDelay(delay = delay) {
        Log.d(TAG, "post(delay = $delay): $event")
        getFlow(event.javaClass.simpleName).emit(event)
    }

    inline fun <reified T : Event> observe(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        crossinline onReceived: (T) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.withStateAtLeast(minState) {
            getFlow(T::class.java.simpleName).collectIn(lifecycleOwner.lifecycleScope) {
                if (it is T) {
                    XLog.d(TAG, "onReceived: $it")
                    onReceived(it)
                }
            }
        }
    }

    interface Event {
        fun post(delay: Long = 0L) = post(this, delay)
    }

}