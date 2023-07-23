package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.databinding.ItemResultBinding
import com.bangkit.wastify.data.model.Result

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
            binding.ivResult.setImageBitmap(data.image)
            binding.tvResult.text = data.classifications[0].name
            binding.tvDate.text = data.createdAt
            binding.root.setOnClickListener { onItemClicked.invoke(data) }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}