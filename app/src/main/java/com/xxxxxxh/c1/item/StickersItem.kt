package com.xxxxxxh.c1.item

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.entity.ResourceEntity
import com.xxxxxxh.c1.utils.MessageEvent
import org.greenrobot.eventbus.EventBus
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.item_stickers)
class StickersItem(view: View) : ItemViewHolder<ResourceEntity>(view) {
    @ViewId(R.id.item_sticker_iv)
    lateinit var img: ImageView
    override fun onSetValues(item: ResourceEntity?, positionInfo: PositionInfo?) {
        Glide.with(context).load(item!!.id).into(img)
        img.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("stickerItem",item))
        }
    }
}