package com.grassterra.fitassist.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grassterra.fitassist.databinding.ItemListArtikelBinding
import com.grassterra.fitassist.response.ListArticleItem
import com.grassterra.fitassist.ui.article.ActivityDetailsArticle

class ArticleAdapter: ListAdapter<ListArticleItem, ArticleAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListArtikelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ActivityDetailsArticle::class.java)
            intent.putExtra("id",item.id.toString())
            holder.itemView.context.startActivity(intent)
        }
    }
    class MyViewHolder(val binding: ItemListArtikelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListArticleItem){
            binding.tvTitle.text = item.title
            binding.tvDesc.text = item.description
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListArticleItem>() {
            override fun areItemsTheSame(oldItem: ListArticleItem, newItem: ListArticleItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListArticleItem, newItem: ListArticleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}