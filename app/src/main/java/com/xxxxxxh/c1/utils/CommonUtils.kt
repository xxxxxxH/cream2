package com.xxxxxxh.c1.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog
import com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.FileOutputStream


object CommonUtils {

    fun saveBitmap(context: Context, name: String, bitmap: Bitmap) {
        val path =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + name + ".jpg"
        val f = File(path)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (out != null) {
                out.flush()
                out.close()
            }
        }
    }

    @SuppressLint("Range")
    fun getAllImgs(context: Context){
        Thread {
            val mediaBeen: MutableList<ImgEntity> = ArrayList()
            val allPhotosTemp: HashMap<String, MutableList<ImgEntity>> =
                HashMap() //所有照片
            val mImageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projImage = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            val mCursor: Cursor? = context.contentResolver.query(
                mImageUri,
                projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                arrayOf("image/jpeg", "image/png"),
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
            )
            if (mCursor != null) {
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    val path: String =
                        mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    val size: Int =
                        mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE)) / 1024
                    val displayName: String =
                        mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    //用于展示相册初始化界面
                    mediaBeen.add(ImgEntity(path, size.toString(), displayName))
                    // 获取该图片的父路径名
                    val dirPath = File(path).parentFile.absolutePath
                    //存储对应关系
                    if (allPhotosTemp.containsKey(dirPath)) {
                        val data: MutableList<ImgEntity> =
                            allPhotosTemp[dirPath]!!
                        data.add(ImgEntity(path, size.toString(), displayName))
                        continue
                    } else {
                        val data: MutableList<ImgEntity> = ArrayList()
                        data.add(ImgEntity(path, size.toString(), displayName))
                        allPhotosTemp[dirPath] = data
                    }
                }
                mCursor.close()
            }
            //更新界面
            runOnUiThread {
               EventBus.getDefault().post(MessageEvent("imgs",mediaBeen))
            }
        }.start()
    }
    fun getScreenSize(activity: Activity):IntArray{
        val result = IntArray(2)
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        result[0] = height
        result[1] = width
        return result
    }

    fun creteProgressDialog(context: Context): AwesomeProgressDialog {
        val dialog = AwesomeProgressDialog(context)
        dialog.setMessage("Please wait")
            .setTitle("Tips")
            .setCancelable(false)
        return dialog
    }
}