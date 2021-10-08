package com.cancer.core.app

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class AppViewModel : ViewModel() {

    private val eventFlows = HashMap<String, MutableSharedFlow<Any?>>()

    private fun getEventFlow(event: String): MutableSharedFlow<Any?> {
        return eventFlows[event]
            ?: MutableSharedFlow<Any?>(extraBufferCapacity = Int.MAX_VALUE).also {
                eventFlows[event] = it
            }
    }

    fun <T> observeEventWithData(
        event: String,
        lifecycleOwner: LifecycleOwner? = null,
        onReceived: (T?) -> Unit
    ) {
        val scope = lifecycleOwner?.lifecycleScope ?: viewModelScope
        scope.launch {
            getEventFlow(event).collect {
                try {
                    onReceived(it as T)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun observeEvent(
        event: String,
        lifecycleOwner: LifecycleOwner? = null,
        onReceived: () -> Unit
    ) {
        val scope = lifecycleOwner?.lifecycleScope ?: viewModelScope
        scope.launch {
            getEventFlow(event).collect {
                    onReceived()
            }
        }
    }

    fun postEvent(event: String, delayMillis: Long) {
        postEventWithData(event, null, delayMillis)
    }

    fun <T> postEventWithData(event: String, data: T?, delayMillis: Long) {
        viewModelScope.launch {
            delay(delayMillis)
            getEventFlow(event).emit(data)
        }
    }

    fun removeEvent(event: String) {
        eventFlows.remove(event)
    }

    fun clearAllEvents() {
        eventFlows.clear()
    }

    fun getEventsCount(): Int {
        return eventFlows.size
    }

}