package com.xxxxxxh.c1.utils

import com.luck.picture.lib.style.BottomNavBarStyle
import com.luck.picture.lib.style.PictureSelectorStyle
import com.luck.picture.lib.style.SelectMainStyle
import com.luck.picture.lib.style.TitleBarStyle

class PictureSelectorUiUtils {
    companion object{
        private var i:PictureSelectorUiUtils?=null
        get() {
            field?:run {
                field = PictureSelectorUiUtils()
            }
            return field
        }
        @Synchronized
        fun get():PictureSelectorUiUtils{
            return i!!
        }
    }

    fun setCustomUiStyle(): PictureSelectorStyle {
        val b  = BottomNavBarStyle()
        b.bottomPreviewNormalText = "Preview"
        b.bottomPreviewSelectText = "Preview"
        val m = SelectMainStyle()
        m.selectNormalText = "Select"
        m.selectText = "Selected"
        val t = TitleBarStyle()
        t.titleCancelText = "Cancel"
        val p = PictureSelectorStyle()
        p.bottomBarStyle = b
        p.selectMainStyle = m
        p.titleBarStyle = t
        return p
    }
}