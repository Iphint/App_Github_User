//package com.arifin.myapplication.ui
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.arifin.myapplication.R
//import com.arifin.myapplication.data.response.ResponseFollowingItem
//import com.bumptech.glide.Glide
//
//class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ViewHolder>(){
//
//    private val followingList: MutableList<ResponseFollowingItem> = mutableListOf()
//
//    fun setData(data: List<ResponseFollowingItem>?) {
//        followingList.clear()
//        if (data != null) {
//            followingList.addAll(data)
//        }
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following, parent, false)
//        return ViewHolder(view)
//    }
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(followingList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return followingList.size
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val followingUsername: TextView = itemView.findViewById(R.id.nameFollowing)
//        private val followingAvatar: ImageView = itemView.findViewById(R.id.imageFollowing)
//
//        fun bind(follower: ResponseFollowingItem) {
//            followingUsername.text = follower.login
//
//            // Load following avatar using Glide or other image loading library
//            Glide.with(itemView.context)
//                .load(follower.avatarUrl)
//                .placeholder(R.drawable.baseline_error_24)
//                .error(R.drawable.baseline_error_24)
//                .into(followingAvatar)
//        }
//    }
//}


package com.arifin.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arifin.myapplication.R
import com.arifin.myapplication.data.response.ResponseFollowingItem
import com.bumptech.glide.Glide

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private val followingList: MutableList<ResponseFollowingItem> = mutableListOf()

    fun setData(data: List<ResponseFollowingItem>?) {
        followingList.clear()
        if (data != null) {
            followingList.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followingList[position])
    }

    override fun getItemCount(): Int {
        return followingList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val followingUsername: TextView = itemView.findViewById(R.id.nameFollowing)
        private val followingAvatar: ImageView = itemView.findViewById(R.id.imageFollowing)

        fun bind(following: ResponseFollowingItem) {
            followingUsername.text = following.login

            Glide.with(itemView.context)
                .load(following.avatarUrl)
                .placeholder(R.drawable.baseline_error_24)
                .error(R.drawable.baseline_error_24)
                .into(followingAvatar)
        }
    }
}
