package com.grassterra.fitassist.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grassterra.fitassist.database.history.HistoryItem
import com.grassterra.fitassist.databinding.ItemListHistoryBinding
import com.grassterra.fitassist.databinding.ListItemLibraryVideoBinding


class HistoryItemAdapter: ListAdapter<HistoryItem, HistoryItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    class MyViewHolder(val binding: ItemListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem){
            binding.tvNameExercise.text = item.exercise
            binding.tvBodyPart.text = item.bodypart
            binding.tvDate.text = item.date
            binding.tvHackSquat.text = item.equipment
            Glide.with(this.itemView.context)
                .load(Uri.parse(item.imageuri))
                .into(this.binding.imgResult)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}