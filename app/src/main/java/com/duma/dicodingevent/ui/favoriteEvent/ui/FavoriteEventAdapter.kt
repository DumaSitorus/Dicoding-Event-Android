package com.duma.dicodingevent.ui.favoriteEvent.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duma.dicodingevent.databinding.FavoriteItemBinding
import com.duma.dicodingevent.ui.detailEvent.DetailEventActivity
import com.duma.dicodingevent.ui.favoriteEvent.database.FavoriteEvent

class FavoriteEventAdapter : ListAdapter<FavoriteEvent, FavoriteEventAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(private val binding: FavoriteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = getItem(position)
                    val intent = Intent(binding.root.context, DetailEventActivity::class.java).apply {
                        putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
                    }
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(event: FavoriteEvent) {
            binding.tvItem.text = event.name
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imageViewEvent)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}

