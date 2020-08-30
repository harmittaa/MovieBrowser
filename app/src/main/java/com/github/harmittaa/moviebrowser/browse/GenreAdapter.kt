package com.github.harmittaa.moviebrowser.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.harmittaa.moviebrowser.databinding.ItemGenreBrowserBinding
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal

class GenreAdapter(private val clickListener: MovieClickListener) :
    ListAdapter<MovieGenreLocal, GenreAdapter.GenreViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<MovieGenreLocal>() {
        override fun areItemsTheSame(oldItem: MovieGenreLocal, newItem: MovieGenreLocal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieGenreLocal,
            newItem: MovieGenreLocal
        ): Boolean {
            return oldItem.items == newItem.items
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGenreBrowserBinding.inflate(inflater, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class GenreViewHolder(private val binding: ItemGenreBrowserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            genre: MovieGenreLocal,
            clickListener: MovieClickListener
        ) {
            binding.apply {
                this.genre = genre
                this.clickListener = clickListener
            }
        }
    }
}
