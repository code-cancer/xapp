package com.cancer.xapp

import com.cancer.core.app.event.XBus

sealed class Event : XBus.Event {
    data class ShowUserName(val userName: String) : Event()
}
