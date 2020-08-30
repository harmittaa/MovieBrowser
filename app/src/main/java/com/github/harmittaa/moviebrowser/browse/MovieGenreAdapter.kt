package com.github.harmittaa.moviebrowser.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.harmittaa.moviebrowser.databinding.ItemMovieGenreBinding
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal

class MovieGenreAdapter(private val clickListener: MovieClickListener) :
    ListAdapter<MovieGenreLocal, MovieGenreAdapter.GenreViewHolder>(
        Companion
    ) {
    private val viewPool = RecyclerView.RecycledViewPool()

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
        val binding = ItemMovieGenreBinding.inflate(inflater, parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position), viewPool, MovieAdapter(clickListener))
    }

    class GenreViewHolder(private val binding: ItemMovieGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            genre: MovieGenreLocal,
            viewPool: RecyclerView.RecycledViewPool,
            adapter: MovieAdapter
        ) {
            adapter.submitList(genre.items)
            binding.apply {
                this.moviesRecyclerview.setRecycledViewPool(viewPool)
                this.moviesRecyclerview.adapter = adapter
                this.genre = genre
            }
        }
    }
}