package com.cancer.core.app.log

import android.util.Log

object XLog {

    private const val TAG = "XApp"

    fun d(msg: String) {
        Log.d("${TAG}.Log", msg)
    }

    fun d(tag: String, msg: String) {
        Log.d("${TAG}.$tag", msg)
    }

}