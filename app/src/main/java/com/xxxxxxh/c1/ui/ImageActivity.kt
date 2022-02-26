package com.xxxxxxh.c1.ui

import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import kotlinx.android.synthetic.main.activity_image.*
import com.xxxxxxh.c1.base.BaseActivity

class ImageActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_image
    }

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(iv)
        cancel.setOnClickListener {
            finish()
        }
        confirm.setOnClickListener {
            finish()
        }
    }
}