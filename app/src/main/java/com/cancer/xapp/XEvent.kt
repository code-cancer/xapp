package com.cancer.xapp

import com.cancer.core.app.event.IEvent

sealed class XEvent: IEvent {
    data class ShowUserName(val userName: String): XEvent()
}
