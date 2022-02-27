package com.xxxxxxh.c1.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog
import com.bumptech.glide.Glide
import com.luck.picture.lib.utils.ToastUtils
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.entity.SlimmingEntity
import com.xxxxxxh.c1.item.SlimmingItem
import com.xxxxxxh.c1.utils.CommonUtils
import com.xxxxxxh.c1.utils.MessageEvent
import kotlinx.android.synthetic.main.activity_slimming.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import uk.co.ribot.easyadapter.EasyRecyclerAdapter
import kotlin.concurrent.thread

class SlimmingActivity : BaseActivity() {

    private val data = ArrayList<SlimmingEntity>()

    private var slimmingAdapter: EasyRecyclerAdapter<SlimmingEntity>? = null

    private var progressDialog: AwesomeProgressDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_slimming
    }

    override fun init() {
        EventBus.getDefault().register(this)
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(slimming_pv)
        cancel.setOnClickListener { finish() }
        save.setOnClickListener {
            progressDialog = CommonUtils.creteProgressDialog(this)
            progressDialog!!.show()
            thread {
                CommonUtils.createBitmapFromView(main)
            }
        }

        getData()
        slimmingAdapter = EasyRecyclerAdapter(this, SlimmingItem::class.java, data)
        recycler.layoutManager = GridLayoutManager(this, 6)
        recycler.adapter = slimmingAdapter
    }

    private fun getData() {
        val id1 = this.resources.getIdentifier("slimming", "mipmap", this.packageName)
        val e1 = SlimmingEntity("slimming", id1, false)
        val id2 = this.resources.getIdentifier("waist", "mipmap", this.packageName)
        val e2 = SlimmingEntity("waist", id2, false)
        val id3 = this.resources.getIdentifier("legs", "mipmap", this.packageName)
        val e3 = SlimmingEntity("legs", id3, false)
        val id4 = this.resources.getIdentifier("legs_length", "mipmap", this.packageName)
        val e4 = SlimmingEntity("legs_length", id4, false)
        val id5 = this.resources.getIdentifier("breast", "mipmap", this.packageName)
        val e5 = SlimmingEntity("breast", id5, false)
        val id6 = this.resources.getIdentifier("shoulder", "mipmap", this.packageName)
        val e6 = SlimmingEntity("shoulder", id6, false)
        data.add(e1)
        data.add(e2)
        data.add(e3)
        data.add(e4)
        data.add(e5)
        data.add(e6)
    }


    @SuppressLint("NotifyDataSetChanged")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: MessageEvent) {
        val msg = e.getMessage()
        when {
            msg[0] == "slimmingItem" -> {
                val entity = msg[1] as SlimmingEntity
                data.forEach {
                    it.select = it.name == entity.name
                }
                slimmingAdapter!!.notifyDataSetChanged()
            }
            msg[0] == "saveSuccess" -> {
                progressDialog!!.hide()
                ToastUtils.showToast(this, "save success")
                startActivity(Intent(this,CreationActivity::class.java))
            }

            msg[0] == "saveError" -> {
                progressDialog!!.hide()
                ToastUtils.showToast(this, "save failed")
            }
        }
    }
}