package com.xxxxxxh.c1.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.entity.ResourceEntity

class StickerAdapter(data: MutableList<ResourceEntity>?) :
    BaseQuickAdapter<ResourceEntity, BaseViewHolder>(R.layout.item_stickers, data) {
    override fun convert(holder: BaseViewHolder, item: ResourceEntity) {
        Glide.with(context).load(item.id).into(holder.getView(R.id.item_sticker_iv))
    }
}