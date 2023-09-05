package com.arifin.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arifin.myapplication.data.entity.GitHubEntity
import com.arifin.myapplication.databinding.ItemLayoutBinding
import com.bumptech.glide.Glide

class FavouriteAdapter(private val onItemClick: (GitHubEntity) -> Unit) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private val data: MutableList<GitHubEntity> = mutableListOf()

    inner class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gitHubUser: GitHubEntity) {
            binding.name.text = gitHubUser.username
            Glide.with(binding.root)
                .load(gitHubUser.avatarUrl)
                .into(binding.image)

            binding.root.setOnClickListener {
                onItemClick(gitHubUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitHubUser = data[position]
        holder.bind(gitHubUser)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(newData: List<GitHubEntity>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}
