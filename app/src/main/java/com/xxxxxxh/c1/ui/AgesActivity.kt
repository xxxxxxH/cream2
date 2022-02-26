package com.xxxxxxh.c1.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.adapter.AgeAdapter
import kotlinx.android.synthetic.main.activity_ages.*
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.ResourceManager

class AgesActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_ages
    }

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(age_pv)
        val data = ResourceManager.get().getResourceByFolder(this,R.mipmap::class.java,"mipmap","icon_age")
        val adapter = AgeAdapter(data)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.adapter = adapter
        adapter.setOnItemClickListener { _, _, _ ->  }
        cancel.setOnClickListener {
            finish()
        }
        save.setOnClickListener {
            finish()
        }
    }
}