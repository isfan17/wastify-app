package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryGridAdapter(
    val onItemClicked: (Category) -> Unit
): ListAdapter<Category, CategoryGridAdapter.CategoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Category) {
            Glide.with(itemView)
                .load(data.icon)
                .into(binding.ivCategoryIcon)
            binding.tvCategoryName.text = data.name
            binding.root.setOnClickListener { onItemClicked.invoke(data) }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}