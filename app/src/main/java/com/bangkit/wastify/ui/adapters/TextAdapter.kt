package com.bangkit.wastify.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.wastify.R

class TextAdapter(private val stringList: List<String>) :
    RecyclerView.Adapter<TextAdapter.TextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_text, parent, false)
        return TextViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val item = stringList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = stringList.size

    inner class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.tv_text)

        fun bind(item: String) {
            textView.text = item
        }
    }
}