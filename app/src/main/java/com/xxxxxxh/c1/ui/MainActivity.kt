package com.xxxxxxh.c1.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.multidex.BuildConfig
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.ToastUtils
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.base.BaseActivity
import com.xxxxxxh.c1.utils.GlideEngine
import com.xxxxxxh.c1.utils.PictureSelectorUiUtils
import com.xxxxxxh.c1.widget.dlg.DialogCallBack
import com.xxxxxxh.c1.widget.dlg.DialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : BaseActivity(), DialogCallBack {

    val pers = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private var isExit = false
    private var exitDlg: AlertDialog? = null
    private var mFilePath: String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun init() {
        requestPermission()
    }

    private fun requestPermission(block: () -> Unit = {}) {
        XXPermissions.with(this)
            .permission(pers)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        gallery.setOnClickListener {
                            startActivity(Intent(this@MainActivity, PhotoActivity::class.java))
                        }
                        camera.setOnClickListener {
                            openCamera()
                        }
                        creation.setOnClickListener {
                            startActivity(Intent(this@MainActivity, CreationActivity::class.java))
                        }
                        share.setOnClickListener {
                            var shareMessage =
                                "Photo Editor" + "" + "\n\nLet me recommend you this application\n\n"
                            shareMessage =
                                "${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                            val shareIntent = Intent(Intent.ACTION_SEND)
                            shareIntent.type = "text/plain"
                            shareIntent.putExtra(
                                Intent.EXTRA_SUBJECT,
                                this@MainActivity.resources.getString(R.string.app_name)
                            )
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                            this@MainActivity.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "choose one"
                                )
                            )
                        }
                    } else {
                        ToastUtils.showToast(
                            this@MainActivity,
                            "some permissions were not granted normally"
                        )
                        finish()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    ToastUtils.showToast(this@MainActivity, "no permissions")
                    finish()
                }
            })
    }

    private fun openCamera() {
        XXPermissions.with(this)
            .permission(Manifest.permission.CAMERA)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    PictureSelector.create(this@MainActivity)
                        .openCamera(SelectMimeType.ofImage())
                        .forResult(object : OnResultCallbackListener<LocalMedia> {
                            override fun onResult(result: ArrayList<LocalMedia>?) {
                            }

                            override fun onCancel() {}
                        })
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    super.onDenied(permissions, never)
                    ToastUtils.showToast(
                        this@MainActivity,
                        "You cannot use the camera without permission"
                    )
                }
            })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    override fun btn1() {
    }

    override fun btn2() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            try {
                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://$mFilePath")
                    )
                )
            } catch (e: Exception) {
                ToastUtils.showToast(this@MainActivity, "scan imgs error")
            }

        }
    }
}