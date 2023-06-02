package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.databinding.ItemCardArticleBinding

class ArticleCardAdapter(
    val onItemClicked: (Article) -> Unit
): ListAdapter<Article, ArticleCardAdapter.ArticleCardViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleCardViewHolder {
        val binding = ItemCardArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleCardViewHolder(private val binding: ItemCardArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Article) {
            binding.ivArticle.setImageResource(data.image)
            binding.tvTitle.text = data.title
            binding.tvSource.text = data.source
            binding.tvPublishedAt.text = data.publishedAt
            binding.root.setOnClickListener { onItemClicked.invoke(data) }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
}