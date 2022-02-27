package com.xxxxxxh.c1.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Message
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog
import com.bumptech.glide.Glide
import com.lcw.library.stickerview.Sticker
import com.luck.picture.lib.utils.ToastUtils
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.entity.ResourceEntity
import com.xxxxxxh.c1.item.StickersItem
import com.xxxxxxh.c1.utils.CommonUtils
import com.xxxxxxh.c1.utils.MessageEvent
import com.xxxxxxh.c1.utils.ResourceManager
import kotlinx.android.synthetic.main.activity_sticker.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import uk.co.ribot.easyadapter.EasyRecyclerAdapter
import kotlin.concurrent.thread

class StickerActivity : BaseActivity() {

    var data: ArrayList<ResourceEntity> = ArrayList()

    private var stickerAdapter: EasyRecyclerAdapter<ResourceEntity>? = null

    private var progressDialog: AwesomeProgressDialog? = null

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

        stickerAdapter =
            EasyRecyclerAdapter(this, StickersItem::class.java, ArrayList<ResourceEntity>())
        recycler.layoutManager = GridLayoutManager(this, 4)
        recycler.adapter = stickerAdapter

        cancel.setOnClickListener {
            finish()
        }
        save.setOnClickListener {
            progressDialog = CommonUtils.creteProgressDialog(this)
            progressDialog!!.show()
            thread {
                CommonUtils.createBitmapFromView(main)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: MessageEvent) {
        val msg = e.getMessage()
        when {
            msg[0] == "stickerItem" -> {
                val selectSticker = msg[1] as ResourceEntity
                val drawable = ContextCompat.getDrawable(this, selectSticker.id)
                val bitmap: BitmapDrawable = drawable as BitmapDrawable
                val s = Sticker(this, bitmap.bitmap)
                stickers.addSticker(s)
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}