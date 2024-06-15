package com.grassterra.fitassist.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grassterra.fitassist.databinding.ListItemLibraryVideoBinding
import com.grassterra.fitassist.response.ListVideoItem
import com.grassterra.fitassist.ui.FullScreenDialogFragment

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
        fun bind(item: ListVideoItem) {
            binding.tvNameExercise.text = item.nameExercise
            binding.tvEquipment.text = item.nameEquipment

            if (!item.urlVideo.isNullOrEmpty()) {
                val url = Uri.parse(item.urlVideo)
                binding.textureViewVideo.setMediaController(
                    android.widget.MediaController(
                        binding.root.context
                    )
                )
                binding.textureViewVideo.setVideoURI(url)
                binding.textureViewVideo.setOnPreparedListener { mediaPlayer ->
                }

                binding.playButton.visibility = View.VISIBLE

                binding.playButton.setOnClickListener {
                    binding.textureViewVideo.start()
                    binding.playButton.visibility = View.GONE
                }

                binding.textureViewVideo.setOnClickListener {
                    if (binding.textureViewVideo.isPlaying) {
                        binding.textureViewVideo.pause()
                        binding.playButton.visibility = View.VISIBLE
                    } else {
                        binding.textureViewVideo.start()
                        binding.playButton.visibility = View.GONE
                    }
                }

                binding.fullScreenButton.setOnClickListener {
                    val fragmentManager = (binding.root.context as AppCompatActivity).supportFragmentManager
                    val fullScreenFragment = FullScreenDialogFragment.newInstance(url)
                    fullScreenFragment.show(fragmentManager, "fullScreenFragment")
                }
            }
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