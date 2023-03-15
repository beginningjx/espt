package com.espt.jx.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.espt.jx.R
import com.espt.jx.dao.HS

class StringAdapter(private val mList: ArrayList<HS>) :
    RecyclerView.Adapter<StringAdapter.ViewHolder>() {

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val view: View = item.findViewById(R.id.view)
        val textView: TextView = item.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FrameLayout.inflate(parent.context, R.layout.item_text, null))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    lateinit var onItemClick: (view: View, position: Int) -> Unit
    lateinit var onItemLongClick: (view: View, position: Int) -> Boolean
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView.text = mList[position].history

        holder.view.setOnClickListener {
            onItemClick.invoke(it, position)
        }

        holder.view.setOnLongClickListener {
            onItemLongClick.invoke(it, position)
        }
    }
}