package com.xxxxxxh.c1.ui

import android.annotation.SuppressLint
import android.os.Environment
import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.luck.picture.lib.utils.ToastUtils
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.entity.ResourceEntity
import com.xxxxxxh.c1.item.StickersItem
import com.xxxxxxh.c1.utils.MessageEvent
import com.xxxxxxh.c1.widget.sticker.DrawableSticker
import kotlinx.android.synthetic.main.activity_sticker.*
import com.xxxxxxh.c1.utils.ResourceManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import uk.co.ribot.easyadapter.EasyRecyclerAdapter
import java.io.File
import kotlin.concurrent.thread

class StickerActivity : BaseActivity() {

    var data: ArrayList<ResourceEntity> = ArrayList()

    private var stickerAdapter:EasyRecyclerAdapter<ResourceEntity>?=null

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> {
                    val data: ArrayList<ResourceEntity> = msg.obj as ArrayList<ResourceEntity>
                    stickerAdapter!!.items = data
                }
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_sticker
    }

    override fun init() {
        EventBus.getDefault().register(this)
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(show_edit_iv)
        thread {
            data = ResourceManager.get()
                .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker")
            val msg = Message()
            msg.what = 1
            msg.obj = data
            handler.sendMessage(msg)
        }

        stickerAdapter = EasyRecyclerAdapter(this,StickersItem::class.java,ArrayList<ResourceEntity>())
        recycler.layoutManager = GridLayoutManager(this, 4)
        recycler.adapter = stickerAdapter

        cancel.setOnClickListener {
            finish()
        }
        save.setOnClickListener {
            Thread {
                val file =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + System.currentTimeMillis() + "_sticker.jpg")
                sticker_view.save(file)
            }.start()
            ToastUtils.showToast(this,"success")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e:MessageEvent){
        val msg = e.getMessage()
        if (msg[0] == "stickerItem"){
            val selectSticker = msg[1] as ResourceEntity
            val drawable = ContextCompat.getDrawable(this, selectSticker.id)
            sticker_view.addSticker(DrawableSticker(drawable))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}