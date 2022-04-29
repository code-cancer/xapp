package com.cancer.core.app.event

/**
 * 事件类需要实现该接口：
 * sealed class XEvent: IEvent {
 *      data class ShowUserName(val userName: String): XEvent()
 * }
 */
interface IEvent