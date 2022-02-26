package com.xxxxxxh.c1.utils

import android.content.Context

object ImagePickerProvider {
    fun getFileProviderName(context: Context): String {
        return context.packageName + ".provider"
    }
}