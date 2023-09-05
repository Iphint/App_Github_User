package com.arifin.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifin.myapplication.R
import com.arifin.myapplication.data.response.ItemsItem
import com.bumptech.glide.Glide

class GithubAdapter(private var userList: List<ItemsItem>) :
    RecyclerView.Adapter<GithubAdapter.ViewHolder>() {

    var onItemClick: ((ItemsItem) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(userList[adapterPosition])
            }
        }
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textView: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]

        // Set data to views
        holder.textView.text = currentItem.login
        Glide.with(holder.itemView.context)
            .load(currentItem.avatarUrl)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_error_24)
            .into(holder.imageView)
    }
    override fun getItemCount(): Int {
        return userList.size
    }
}

