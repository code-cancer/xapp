package com.cancer.xapp

import com.cancer.core.app.event.XBus

sealed class XEvent: XBus.Event() {
    data class ShowUserName(val userName: String): XEvent()
}
