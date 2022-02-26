package com.xxxxxxh.c1.item

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xxxxxxh.c1.R
import com.xxxxxxh.c1.entity.SlimmingEntity
import com.xxxxxxh.c1.utils.MessageEvent
import org.greenrobot.eventbus.EventBus
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.item_slim)
class SlimmingItem(view: View) : ItemViewHolder<SlimmingEntity>(view) {
    @ViewId(R.id.itemBg)
    lateinit var img: ImageView

    @ViewId(R.id.itemName)
    lateinit var name: TextView
    override fun onSetValues(item: SlimmingEntity?, positionInfo: PositionInfo?) {
//        Glide.with(context).load(item!!.id).into(img)

        val nameStr = item!!.name
        if (item.select) {
            val id = context.resources.getIdentifier(
                "${nameStr}_${positionInfo!!.position+1}",
                "mipmap",
                context.packageName
            )
            img.setBackgroundResource(id)
        } else {
            img.setBackgroundResource(item.id)
        }
        name.text = nameStr
        img.setOnClickListener {
            EventBus.getDefault().post(MessageEvent("slimmingItem", item))
        }
    }
}