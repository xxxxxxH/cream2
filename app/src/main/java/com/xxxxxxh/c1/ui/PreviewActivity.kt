package com.xxxxxxh.c1.ui

import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_preview

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(imageview)
    }
}