package com.sherloki.devkit.ktx

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sherloki.devkit.*

open class KtxActivity : AppCompatActivity() {


    var isHandle = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun onDismiss() {

    }

}