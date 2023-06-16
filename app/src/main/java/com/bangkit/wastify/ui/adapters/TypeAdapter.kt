package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.databinding.ItemTypeBinding
import com.bumptech.glide.Glide

class TypeAdapter(
    val onItemClicked: (Type) -> Unit
): ListAdapter<Type, TypeAdapter.TypeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val binding = ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TypeViewHolder(private val binding: ItemTypeBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Type) {
            Glide.with(itemView)
                .load(data.icon)
                .into(binding.ivTypeIcon)
            binding.tvTypeName.text = data.name
            binding.root.setOnClickListener { onItemClicked.invoke(data) }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Type>() {
        override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem == newItem
        }
    }
}