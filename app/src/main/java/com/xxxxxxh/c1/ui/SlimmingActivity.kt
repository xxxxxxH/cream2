package com.xxxxxxh.c1.ui

import android.app.AlertDialog
import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import kotlinx.android.synthetic.main.activity_slimming.*
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.widget.dlg.DialogCallBack
import com.xxxxxxh.c1.widget.dlg.DialogUtils

class SlimmingActivity : BaseActivity() ,DialogCallBack{

    private var saveDlg: AlertDialog? = null

    private var shareDlg: AlertDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_slimming
    }

    override fun init() {
        val url = intent.getStringExtra("url") as String
        Glide.with(this).load(url).into(slimming_pv)
        cancel.setOnClickListener { finish() }
        save.setOnClickListener {
            saveDlg = DialogUtils.createExitDlg(
                this, "Saving",
                b1 = true,
                b2 = false,
                callBack = this
            )
            saveDlg!!.show()
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