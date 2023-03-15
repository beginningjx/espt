package com.espt.jx.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.espt.jx.R
import com.espt.jx.dao.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeAdapter(mList: List<Data>) : BaseQuickAdapter<Data, HomeAdapter.VH>() {
    init {
        isEmptyViewEnable = true
        CoroutineScope(Dispatchers.Main).launch {
            setEmptyViewLayout(context, R.layout.item_home_image)
        }
        submitList(mList)
    }

    class VH(parent: ViewGroup) :
        RecyclerView.ViewHolder(FrameLayout.inflate(parent.context, R.layout.item_home, null)) {
        val mPicture: ImageView = itemView.findViewById(R.id.picture)
        val mIntroduction: TextView = itemView.findViewById(R.id.introduction)
        val mMoney: TextView = itemView.findViewById(R.id.money)
        val mQqPicture: ImageView = itemView.findViewById(R.id.qq_picture)
        val mQqName: TextView = itemView.findViewById(R.id.qq_name)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int, item: Data?) {
        val picture = item?.picture?.split("&&&")?.get(0)
        val lp = holder.mPicture.layoutParams

        Glide.with(holder.mPicture).asBitmap().load(picture).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                if (resource.height > resource.width + 100) {
                    lp.height = 700
                }
                Glide.with(holder.mPicture).load(resource).into(holder.mPicture)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

        holder.mIntroduction.text = item?.introduction
        holder.mMoney.text = item?.money
        Glide.with(holder.mQqPicture).load(item?.qq_picture).into(holder.mQqPicture)
        holder.mQqName.text = item?.qq_name
    }
}