package com.xxxxxxh.c1.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.ToastUtils
import com.xxxxxxh.c1.BuildConfig
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

    private fun openGallery(targetAc: Int) {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setSelectorUIStyle(PictureSelectorUiUtils.get().setCustomUiStyle())
            .setImageEngine(GlideEngine().createGlideEngine())
            .setMaxSelectNum(1)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    result?.let {
                        val url = result[0].realPath
                        var intent: Intent? = null
                        when (targetAc) {
                            0 -> {
                                intent = Intent(this@MainActivity, StickerActivity::class.java)
                                intent.putExtra("url", url)
                            }
                            1 -> {
                                intent = Intent(this@MainActivity, SlimmingActivity::class.java)
                                intent.putExtra("url", url)
                            }
                            else -> return@let
                        }
                        startActivity(intent)
                    }
                }

                override fun onCancel() {}
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
//                                result?.let {
//                                    val url = result[0].realPath
//                                    intent = Intent(this@MainActivity, ImageActivity::class.java)
//                                    intent.putExtra("url", url)
//                                    startActivity(intent)
//                                }
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
        if (!isExit) {
            exitDlg = DialogUtils.createExitDlg(
                this, "Are you sure to exit the application?", b1 = true, b2 = true, callBack = this
            )
            exitDlg!!.show()
        }
//        super.onBackPressed()
    }


    override fun btn1() {
        isExit = true
        if (exitDlg != null && exitDlg!!.isShowing)
            exitDlg!!.dismiss()
        exitProcess(0)
//        finish()
    }

    override fun btn2() {
        if (exitDlg != null && exitDlg!!.isShowing)
            exitDlg!!.dismiss()
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