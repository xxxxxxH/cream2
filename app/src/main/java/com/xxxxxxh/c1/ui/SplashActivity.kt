package com.xxxxxxh.c1.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.sherloki.devkit.*
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.MessageEvent
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun init() {
        EventBus.getDefault().register(this)
        lifecycleScope.requestConfig {
            if (isLogin) {
                jumpToMain()
            } else {
                if (configEntity.needLogin()) {
                    if (configEntity.needDeepLink() && configEntity.faceBookId().isNotBlank()) {
                        fetchAppLink(configEntity.faceBookId()) {
                            "initFaceBook $it".loge()
                            it?.let {
                                runOnUiThread {
                                    login.isVisible = true
                                }
                            } ?: kotlin.run {
                                jumpToMain()
                            }
                        }
                    } else {
                        login.isVisible = true
                    }
                } else {
                    jumpToMain()
                }
            }
        }

        login.setOnClickListener {
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
//            finish()
        }

    }

    private fun jumpToMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageEvent) {
        val msg = event.getMessage()
        if (msg[0] == "destroy") {
            finish()
        }
    }
}