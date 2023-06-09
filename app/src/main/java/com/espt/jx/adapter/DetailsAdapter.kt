package com.espt.jx.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.espt.jx.R


class DetailsAdapter(private val mList: List<String>) :
    RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val mView: ImageView = item.findViewById(R.id.picture)
    }

    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(FrameLayout.inflate(parent.context, R.layout.item_image, null))
    }


    override fun getItemCount(): Int = mList.size - 1

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        Glide.with(holder.mView).asBitmap().load(mList[position])
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    try {
                        holder.mView.setImageBitmap(resource)
                    } catch (e: Exception) {
                        Glide.with(holder.mView).load(mList[position]).into(holder.mView)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

    }
}