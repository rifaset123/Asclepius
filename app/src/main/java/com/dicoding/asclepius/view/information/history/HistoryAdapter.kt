package com.dicoding.asclepius.view.information.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ItemCardViewBinding

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(history: HistoryEntity) {
            binding.apply {
                // Bind data to views
                with(binding){
                    itemTitle.text = history.result
                    itemConfidant.text = history.detail
                    itemDate.text = "Analyzed at " + history.analyzeTime
                }
                Glide.with(binding.root.context)
                    .load(history.uriImage) // URL Gambar
                    .centerCrop()
                    .placeholder(R.drawable.ng)
                    .into(binding.itemImage) // imageView
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HistoryEntity> =
            object : DiffUtil.ItemCallback<HistoryEntity>() {
                override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                    return oldItem.analyzeTime == newItem.analyzeTime
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

