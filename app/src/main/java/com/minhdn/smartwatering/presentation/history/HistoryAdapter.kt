package com.minhdn.smartwatering.presentation.history

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minhdn.smartwatering.data.local.entity.HistoryEntity
import com.minhdn.smartwatering.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyEntity: HistoryEntity) {
            binding.tvTime.text = historyEntity.formatTime()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        Log.d("myptl", "onBindViewHolder: $position")
        holder.bind(getItem(position))
    }
}