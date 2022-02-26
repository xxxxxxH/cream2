package com.xxxxxxh.c1.widget.dlg

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.utils.MessageEvent
import org.greenrobot.eventbus.EventBus

object DialogUtils {
    fun createExitDlg(
        context: Context,
        content: String,
        b1: Boolean,
        b2: Boolean,
        callBack: DialogCallBack
    ): AlertDialog {
        val dlg = AlertDialog.Builder(context).create()
        dlg.setCancelable(false)
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_exit, null)
        dlg.setView(v)
        v.findViewById<TextView>(R.id.title).text = content
        val btn1 = v.findViewById<TextView>(R.id.yes)
        btn1.visibility = if (b1) View.VISIBLE else View.GONE
        btn1.setOnClickListener { callBack.btn1() }
        val btn2 = v.findViewById<TextView>(R.id.no)
        btn2.visibility = if (b2) View.VISIBLE else View.GONE
        btn2.setOnClickListener { callBack.btn2() }
        return dlg
    }

    fun createShareDlg(context: Context): AlertDialog {
        val dlg = AlertDialog.Builder(context).create()
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_share, null)
        dlg.setView(v)
        return dlg
    }

    fun createOptionDlg(context: Context):AlertDialog{
        val dlg = AlertDialog.Builder(context).create()
        val v = LayoutInflater.from(context).inflate(R.layout.dialog_select_option, null)
        dlg.setView(v)
        val btn1 = v.findViewById<LinearLayout>(R.id.stickers)
        btn1.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("stickers"))
        }
        val btn2 = v.findViewById<LinearLayout>(R.id.slimming)
        btn2.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("slimming"))
        }
        return dlg
    }
}