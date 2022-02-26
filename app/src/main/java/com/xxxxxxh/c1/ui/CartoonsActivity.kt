package com.xxxxxxh.c1.ui

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.adapter.CartoonAdapter
import com.xxxxxxh.c1.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_cartoons.*
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.ResourceManager

class CartoonsActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_cartoons
    }

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(slimming_pv)
        val data = ResourceManager.get()
            .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "cartoon")
        val adapter = CartoonAdapter(data)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        adapter.setOnItemClickListener { _, _, _ ->  }
        cancel.setOnClickListener {
            finish()
        }
        save.setOnClickListener {
            val d = slimming_pv.drawable as Drawable
            val bd: BitmapDrawable = d as BitmapDrawable
            val b = bd.bitmap
            CommonUtils.saveBitmap(this, System.currentTimeMillis().toString(), b)
        }
    }
}