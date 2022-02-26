package com.xxxxxxh.c1.ui

import android.app.AlertDialog
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.adapter.StickerAdapter
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.entity.ResourceEntity
import com.xxxxxxh.c1.widget.dlg.DialogCallBack
import com.xxxxxxh.c1.widget.dlg.DialogUtils
import com.xxxxxxh.c1.widget.sticker.DrawableSticker
import kotlinx.android.synthetic.main.activity_sticker.*
import com.xxxxxxh.c1.utils.ResourceManager
import java.io.File

class StickerActivity : BaseActivity(), DialogCallBack {

    var data: ArrayList<ResourceEntity> = ArrayList()

    private var saveDlg: AlertDialog? = null

    private var shareDlg: AlertDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_sticker
    }

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(show_edit_iv)
        data = ResourceManager.get()
            .getResourceByFolder(this, R.mipmap::class.java, "mipmap", "sticker")
        recycler.layoutManager = GridLayoutManager(this, 4)
        val adapter = StickerAdapter(data)
        recycler.adapter = adapter
        adapter.setOnItemClickListener { _, _, p ->
            val selectSticker = data[p]
            val drawable = ContextCompat.getDrawable(this, selectSticker.id)
            sticker_view.addSticker(DrawableSticker(drawable))
        }
        cancel.setOnClickListener {
            finish()
        }
        save.setOnClickListener {
            saveDlg = DialogUtils.createExitDlg(
                this, "Saving",
                b1 = true,
                b2 = false,
                callBack = this
            )
            saveDlg!!.show()
            Thread {
                val file =
                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + System.currentTimeMillis() + "_sticker.jpg")
                sticker_view.save(file)
            }.start()
        }
    }

    override fun btn1() {
        if (saveDlg!=null && saveDlg!!.isShowing)
            saveDlg!!.dismiss()
        shareDlg = DialogUtils.createShareDlg(this)
        shareDlg!!.show()
    }

    override fun btn2() {

    }
}