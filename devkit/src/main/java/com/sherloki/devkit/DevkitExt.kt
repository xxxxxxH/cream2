package com.sherloki.devkit

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sherloki.devkit.entity.ConfigEntity
import com.sherloki.devkit.entity.UpdateEntity
import com.sherloki.devkit.ktx.Ktx
import com.tencent.mmkv.MMKV

fun Context.jumpToWebByDefault(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
    startActivity(it)
}

val globalMetrics by lazy {
    Resources.getSystem().displayMetrics
}

val globalWidth by lazy {
    globalMetrics.widthPixels
}

val globalHeight by lazy {
    globalMetrics.heightPixels
}

var account
    get() = mmkv.getString(DevkitConstant.KEY_ACCOUNT, "") ?: ""
    set(value) {
        mmkv.putString(DevkitConstant.KEY_ACCOUNT, value)
    }

private var config
    get() = mmkv.getString(DevkitConstant.KEY_CONFIG, "") ?: ""
    set(value) {
        mmkv.putString(DevkitConstant.KEY_CONFIG, value)
    }

var configEntity
    get() = (config.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, ConfigEntity::class.java)
    }
    set(value) {
        config = gson.toJson(value)
    }

var adInvokeTime
    get() = mmkv.getInt(DevkitConstant.KEY_AD_INVOKE_TIME, 0)
    set(value) {
        mmkv.putInt(DevkitConstant.KEY_AD_INVOKE_TIME, value)
    }

var adRealTime
    get() = mmkv.getInt(DevkitConstant.KEY_AD_REAL_TIME, 0)
    set(value) {
        mmkv.putInt(DevkitConstant.KEY_AD_REAL_TIME, value)
    }

private var adShown
    get() = mmkv.getString(DevkitConstant.KEY_AD_SHOWN, "") ?: ""
    set(value) {
        mmkv.putString(DevkitConstant.KEY_AD_SHOWN, value)
    }

var adShownList
    get() = (adShown.ifBlank {
        "{}"
    }).let {
        gson.fromJson<List<Boolean>>(it, object : TypeToken<List<Boolean>>() {}.type)
    }
    set(value) {
        adShown = gson.toJson(value)
    }

var adShownIndex
    get() = mmkv.getInt(DevkitConstant.KEY_AD_SHOWN_INDEX, 0)
    set(value) {
        mmkv.putInt(DevkitConstant.KEY_AD_SHOWN_INDEX, value)
    }

var adLastTime
    get() = mmkv.getLong(DevkitConstant.KEY_AD_LAST_TIME, 0)
    set(value) {
        mmkv.putLong(DevkitConstant.KEY_AD_LAST_TIME, value)
    }

private var update
    get() = mmkv.getString(DevkitConstant.KEY_UPDATE, "") ?: ""
    set(value) {
        mmkv.putString(DevkitConstant.KEY_UPDATE, value)
    }

var updateEntity
    get() = (update.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, UpdateEntity::class.java)
    }
    set(value) {
        update = gson.toJson(value)
    }

var password
    get() = mmkv.getString(DevkitConstant.KEY_PASSWORD, "") ?: ""
    set(value) {
        mmkv.putString(DevkitConstant.KEY_PASSWORD, value)
    }

var isLogin
    get() = mmkv.getBoolean(DevkitConstant.KEY_IS_LOGIN, false)
    set(value) {
        mmkv.putBoolean(DevkitConstant.KEY_IS_LOGIN, value)
    }

val gson by lazy {
    Gson()
}

val mmkv by lazy {
    MMKV.mmkvWithID("devkit")
}

val app by lazy {
    Ktx.getInstance().app
}

fun <T> T.loge(tag: String = "defaultTag") {
    if (BuildConfig.DEBUG) {
        var content = toString()
        val segmentSize = 3 * 1024
        val length = content.length.toLong()
        if (length <= segmentSize) {
            Log.e(tag, content)
        } else {
            while (content.length > segmentSize) {
                val logContent = content.substring(0, segmentSize)
                content = content.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, content)
        }
    }
}