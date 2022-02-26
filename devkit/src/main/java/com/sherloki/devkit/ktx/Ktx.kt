package com.sherloki.devkit.ktx

import android.app.Application
import android.os.Build
import android.util.Log
import android.webkit.WebView
import com.sherloki.devkit.loge
import com.tencent.mmkv.MMKV
import kotlin.system.measureTimeMillis

class Ktx private constructor(application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: Ktx? = null

        fun initialize(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Ktx(application).apply { INSTANCE = this }
            }


        fun getInstance() =
            INSTANCE ?: throw NullPointerException("Have you invoke initialize() before?")
    }

    val app = application

    fun initStartUp() {
        measureTimeMillis {
            MMKV.initialize(app)
        }.let {
            "application initTime -> ${it}".loge()
        }
    }

}