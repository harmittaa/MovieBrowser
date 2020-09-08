package com.github.harmittaa.moviebrowser.browse.genres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.harmittaa.moviebrowser.browse.MovieClickListener
import com.github.harmittaa.moviebrowser.databinding.ItemGenreBrowserBinding
import com.github.harmittaa.moviebrowser.domain.GenreLocal

class GenreAdapter(private val clickListener: MovieClickListener) :
    ListAdapter<GenreLocal, GenreAdapter.GenreViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<GenreLocal>() {
        override fun areItemsTheSame(oldItem: GenreLocal, newItem: GenreLocal): Boolean {
            return oldItem.genreId == newItem.genreId
        }

        override fun areContentsTheSame(
            oldItem: GenreLocal,
            newItem: GenreLocal
        ): Boolean {
            return oldItem == newItem
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
            genre: GenreLocal,
            clickListener: MovieClickListener
        ) {
            binding.apply {
                this.genre = GenreLocal(genre.genreId, genre.name)
                this.clickListener = clickListener
            }
        }
    }
}
