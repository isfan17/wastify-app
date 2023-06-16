package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.databinding.ItemResultBinding
import com.bumptech.glide.Glide

class ResultGridAdapter(
    val onItemClicked: (Result) -> Unit
): ListAdapter<Result, ResultGridAdapter.ResultViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ResultViewHolder(private val binding: ItemResultBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Result) {
            Glide.with(itemView)
                .load(data.imageUrl)
                .into(binding.ivResult)
            binding.tvResult.text = data.name
            binding.tvDate.text = data.createdAt
            binding.root.setOnClickListener { onItemClicked.invoke(data) }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}