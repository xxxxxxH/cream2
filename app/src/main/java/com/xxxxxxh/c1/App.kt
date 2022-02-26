package com.xxxxxxh.c1

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.sherloki.devkit.ktx.Ktx


class App : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Ktx.initialize(this)
    }

    override fun onCreate() {
        super.onCreate()
        Ktx.getInstance().initStartUp()
    }

}