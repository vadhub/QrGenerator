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


class MenuAdapter(private val menuItem: List<Pair<Drawable, Int>>) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_menu, parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.image.setImageDrawable(menuItem.get(position).first)
        holder.text.setText(menuItem.get(position).second)
    }

    override fun getItemCount() = menuItem.size

    class MyViewHolder(item: View) : ViewHolder(item) {

        val image = item.findViewById<ImageView>(R.id.menuImage)
        val text = item.findViewById<TextView>(R.id.menuTitle)

    }
}