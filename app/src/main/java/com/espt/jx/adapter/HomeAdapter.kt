package com.espt.jx.adapter

import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.espt.jx.R
import com.espt.jx.dao.Data
import com.espt.jx.model.Home

class HomeAdapter(private val mList: List<Data>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val mView: View = item.findViewById(R.id.view)
        val mPicture: ImageView = item.findViewById(R.id.picture)
        val mIntroduction: TextView = item.findViewById(R.id.introduction)
        val mMoney: TextView = item.findViewById(R.id.money)
        val mQqPicture: ImageView = item.findViewById(R.id.qq_picture)
        val mQqName: TextView = item.findViewById(R.id.qq_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        FrameLayout.inflate(parent.context, R.layout.item_home, null)
    )

    override fun getItemCount(): Int = mList.size

    lateinit var onItemClick: (view: View, position: Int) -> Unit
    lateinit var onItemLongClick: (view: View, position: Int) -> Boolean
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = mList[position].picture.split("&&&")[0]
        val lp = holder.mPicture.layoutParams
        val decodeFile = BitmapFactory.decodeFile(picture)
        if (decodeFile != null) {
            // 宽大于高说明是长图
            if (decodeFile.height > decodeFile.width + 100) {
                lp.height = 600
            }
        }
        Glide.with(holder.mPicture).load(picture).into(holder.mPicture)
        holder.mIntroduction.text = mList[position].introduction
        holder.mMoney.text = mList[position].money
        Glide.with(holder.mQqPicture).load(mList[position].qq_picture)
            .into(holder.mQqPicture)
        holder.mQqName.text = mList[position].qq_name
        // 点击事件
        holder.mView.setOnClickListener {
            onItemClick.invoke(it, position)
        }
        holder.mView.setOnLongClickListener {
            onItemLongClick.invoke(it, position)
        }
    }
}