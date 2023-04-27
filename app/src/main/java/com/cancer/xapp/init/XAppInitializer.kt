package com.cancer.xapp.init

import android.content.Context
import androidx.startup.Initializer
import com.cancer.core.app.XApp

class XAppInitializer : Initializer<XApp> {

    override fun create(context: Context): XApp {
        XApp.init(context)
        return XApp
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}