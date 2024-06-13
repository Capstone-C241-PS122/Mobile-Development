package com.grassterra.fitassist.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grassterra.fitassist.databinding.ListItemLibraryVideoBinding
import com.grassterra.fitassist.response.ListVideoItem

class LibraryVideoAdapter: ListAdapter<ListVideoItem, LibraryVideoAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListItemLibraryVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class MyViewHolder(val binding: ListItemLibraryVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListVideoItem){
            binding.tvNameExercise.text = item.nameExercise
            binding.tvEquipment.text = item.nameEquipment
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListVideoItem>() {
            override fun areItemsTheSame(oldItem: ListVideoItem, newItem: ListVideoItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListVideoItem, newItem: ListVideoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}