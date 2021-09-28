package com.xapp.tools.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class XApplication : Application(), ViewModelStoreOwner{

    companion object {

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: XApplication

    }

    private val viewModelStore = ViewModelStore()

    val viewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(instance)).get(AppViewModel::class.java)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = this
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun getViewModelStore(): ViewModelStore {
        return viewModelStore
    }

}