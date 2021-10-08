package com.cancer.core.app

import androidx.lifecycle.LifecycleOwner

object FlowEventBus {

    private val appViewModel = XApplication.instance.viewModel

    fun postEvent(event: String, delayMillis: Long = 0){
        postEventWithData(event, null, delayMillis)
    }

    fun <T> postEventWithData(event: String, data: T, delayMillis: Long = 0){
        appViewModel.postEventWithData(event, data, delayMillis)
    }

    fun <T> observeEventWithData(
        lifecycleOwner: LifecycleOwner? = null,
        event: String,
        onReceived: (T?) -> Unit
    ){
        appViewModel.observeEventWithData<T>(event, lifecycleOwner, onReceived)
    }

    fun observeEvent(
        lifecycleOwner: LifecycleOwner? = null,
        event: String,
        onReceived: () -> Unit
    ){
        appViewModel.observeEvent(event, lifecycleOwner, onReceived)
    }

    fun removeEvent(event: String){
        appViewModel.removeEvent(event)
    }

    fun clearAllEvents(event: String){
        appViewModel.clearAllEvents()
    }

    fun getEventsCount(): Int{
        return appViewModel.getEventsCount()
    }

}