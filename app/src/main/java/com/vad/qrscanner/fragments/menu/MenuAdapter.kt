package com.vad.qrscanner.fragments.menu

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vad.qrscanner.R


class MenuAdapter(private val menuItem: List<Pair<Drawable, String>>) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    private lateinit var onItemMenuClickListener: OnItemMenuClickListener

    interface OnItemMenuClickListener {
        fun onClick(position: Int)
    }

    fun setOnItemMenuClickListener(onItemMenuClickListener: OnItemMenuClickListener) {
        this.onItemMenuClickListener = onItemMenuClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageDrawable(menuItem.get(position).first)
        holder.text.setText(menuItem.get(position).second)
    }

    override fun getItemCount() = menuItem.size

    inner class MyViewHolder(item: View) : ViewHolder(item) {

        val image = item.findViewById<ImageView>(R.id.menuImage)
        val text = item.findViewById<TextView>(R.id.menuTitle)

        init {
            item.setOnClickListener{
                onItemMenuClickListener.onClick(adapterPosition)
            }
        }

    }
}