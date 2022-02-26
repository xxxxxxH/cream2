package com.xxxxxxh.c1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.adapter.PhotoAdapter
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.CommonUtils
import com.xxxxxxh.c1.utils.ImgEntity
import com.xxxxxxh.c1.utils.MessageEvent
import com.xxxxxxh.c1.widget.dlg.DialogUtils
import kotlinx.android.synthetic.main.activity_photo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PhotoActivity : BaseActivity() {
    private var progressDialog: AwesomeProgressDialog? = null
    private var optionDialog:AlertDialog?=null
    private var url = ""
    override fun getLayoutId()= R.layout.activity_photo

    override fun init() {
        progressDialog = CommonUtils.creteProgressDialog(this)
        progressDialog!!.show()
        EventBus.getDefault().register(this)
        CommonUtils.getAllImgs(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e:MessageEvent){
        val msg = e.getMessage()
        when {
            msg[0] == "imgs" ->{
                progressDialog!!.hide()
                val data = msg[1] as ArrayList<ImgEntity>
                val adapter = PhotoAdapter(data)
                recycler.layoutManager = GridLayoutManager(this,2)
                recycler.adapter = adapter
                adapter.setOnItemClickListener { a, _, position ->
                    optionDialog = DialogUtils.createOptionDlg(this)
                    url = (a.data[position] as ImgEntity).path
                    optionDialog!!.show()
                }
            }
            msg[0] == "stickers" -> {
                if (url != ""){
                    optionDialog!!.dismiss()
                    val i = Intent(this,StickerActivity::class.java)
                    i.putExtra("url",url)
                    startActivity(i)
                }
            }
            msg[0] == "slimming" ->{
                if (url != ""){
                    optionDialog!!.dismiss()
                    val i = Intent(this,SlimmingActivity::class.java)
                    i.putExtra("url",url)
                    startActivity(i)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}