package com.espt.jx.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espt.jx.R
import com.espt.jx.ui.activity.SellActivity

class SellAdapter(private val mList: ArrayList<String>) :
    RecyclerView.Adapter<SellAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val imageView: ImageView = item.findViewById(R.id.imageView)
    }

    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(FrameLayout.inflate(parent.context, R.layout.item_sell, null))
    }

    override fun getItemCount(): Int {
        return when (mList.size) {
            0 -> {
                mList.add("add_image")
                1
            }
            7 -> mList.size - 1
            else -> mList.size
        }
    }

    lateinit var onItemClick: (view: View, position: Int) -> Unit
    lateinit var onItemLongClick: (view: View, position: Int) -> Boolean
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (mList[position] == "add_image") {
            Glide.with(holder.imageView)
                .load(ContextCompat.getDrawable(mContext, R.drawable.ic_add_image))
                .into(holder.imageView)
        } else {
            Glide.with(holder.imageView).load(mList[position]).into(holder.imageView)
        }

        holder.imageView.setOnClickListener {
            if (position + 1 == itemCount) {
                onItemClick.invoke(it, position)
            }
        }

        holder.imageView.setOnLongClickListener {
            if (position + 1 != mList.size) {
                onItemLongClick.invoke(it, position)
            } else {
                false
            }
        }
    }
}