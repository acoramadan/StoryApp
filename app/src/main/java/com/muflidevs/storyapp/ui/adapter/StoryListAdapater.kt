package com.muflidevs.storyapp.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.muflidevs.storyapp.data.remote.response.Story
import com.muflidevs.storyapp.databinding.ItemListBinding

class StoryListAdapater(private val context: Context, private val onItemClicked: (Story) -> Unit):
    ListAdapter<Story,StoryListAdapater.StoryViewHolder>(DIFF_CALLBACK) {

    inner class StoryViewHolder(private var binding: ItemListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: Story, onItemClicked: (Story) -> Unit) {
            Log.d("StoryAdapter", "Binding story: ${stories.name}")
            binding.name.text = stories.name
            binding.deskripsi.text = stories.description
            Glide.with(context)
                .load(stories.photoUrl)
                .into(binding.image)
            binding.root.setOnClickListener {
                onItemClicked(stories)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val detailStory = getItem(position)
        holder.bind(detailStory,onItemClicked)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

        }
    }
}