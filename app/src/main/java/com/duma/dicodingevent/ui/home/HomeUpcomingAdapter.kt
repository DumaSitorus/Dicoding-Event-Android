package com.duma.dicodingevent.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.duma.dicodingevent.data.response.ListEventsItem
import com.duma.dicodingevent.databinding.SmallItemBinding
import com.duma.dicodingevent.ui.detailEvent.DetailEventActivity

class HomeUpcomingAdapter:ListAdapter<ListEventsItem, HomeUpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = SmallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val finishEvent = getItem(position)
        holder.bind(finishEvent)
    }

    inner class MyViewHolder(private val binding: SmallItemBinding): RecyclerView.ViewHolder(binding.root) {
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
        fun bind(review: ListEventsItem){
            binding.tvItem.text = review.name
            Glide.with(binding.root.context)
                .load(review.mediaCover)
                .into(binding.imageViewEvent)

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}