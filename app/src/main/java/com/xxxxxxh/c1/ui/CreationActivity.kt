package com.xxxxxxh.c1.ui

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.tencent.mmkv.MMKV
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.item.PictureItem
import kotlinx.android.synthetic.main.activity_creation.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import uk.co.ribot.easyadapter.EasyRecyclerAdapter
import com.xxxxxxh.c1.utils.MessageEvent

class CreationActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_creation

    override fun init() {
        EventBus.getDefault().register(this)
        initData()
    }

    private fun initData() {
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("keys") as HashSet?
        val data = ArrayList<String>()
        if (keySet != null) {
            for (item in keySet) {
                MMKV.defaultMMKV()!!.decodeString(item)?.let {
                    data.add(it)
                }
            }
        }
        if (data.size > 0) {
            val adapter = EasyRecyclerAdapter(this, PictureItem::class.java, data)
            recycler.layoutManager = GridLayoutManager(this, 3)
            recycler.adapter = adapter
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: MessageEvent) {
        val msg = e.getMessage()
        if (msg[0] == "picItem") {
            val i = Intent(this, PreviewActivity::class.java)
            i.putExtra("url", msg[1] as String)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}