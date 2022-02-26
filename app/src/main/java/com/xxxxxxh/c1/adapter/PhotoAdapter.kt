package com.xxxxxxh.c1.adapter

import android.app.Activity
import android.graphics.Color
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.utils.CommonUtils
import com.xxxxxxh.c1.utils.ImgEntity

class PhotoAdapter(data: MutableList<ImgEntity>?) :
    BaseQuickAdapter<ImgEntity, BaseViewHolder>(R.layout.item_photo, data) {
    override fun convert(holder: BaseViewHolder, item: ImgEntity) {
        val root = holder.getView<RelativeLayout>(R.id.itemRoot)
        root.layoutParams.apply {
            width = (CommonUtils.getScreenSize(context as Activity)[1] - 36) / 2
            height = CommonUtils.getScreenSize(context as Activity)[0] / 5
        }
        Glide.with(context).load(item.path).placeholder(R.mipmap.ic_launcher_round).into(holder.getView(R.id.itemBg))
        holder.setText(R.id.itemName, item.name)
    }
}