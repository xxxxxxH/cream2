package com.xxxxxxh.c1.base

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sherloki.devkit.ktx.KtxActivity
import com.xxxxxxh.c1.R
import org.xutils.x

abstract class BaseActivity : KtxActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        x.Ext.init(this.application)
        init()
    }

    abstract fun getLayoutId(): Int

    abstract fun init()

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun isBackground(): Boolean {
        val activityManager = this
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager
            .runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == this.packageName) {
                return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}