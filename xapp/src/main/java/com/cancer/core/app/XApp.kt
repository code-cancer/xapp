package com.cancer.core.app

import android.app.Application
import android.content.Context
import com.cancer.core.app.log.XLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object XApp {

    lateinit var appContext: Context
    lateinit var appScope: CoroutineScope

    private val scopeErrorHandler = CoroutineExceptionHandler { _, throwable ->
        XLog.d("CoroutineError", "${throwable.message}")
    }

    fun init(context: Context) {
        appContext = context
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + scopeErrorHandler)
    }

}