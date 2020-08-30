package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenre
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrowseFragment : Fragment() {
    val viewModel: BrowseViewModel by viewModel()
    private val adapter = MovieGenreAdapter()
    var items = getData()
    var changeFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBrowseBinding.inflate(inflater, container, false)
        binding.browseRecycler.apply {
            this.adapter = this@BrowseFragment.adapter
        }
        adapter.submitList(items)

        binding.refreshButton.setOnClickListener {
            changeFirst = !changeFirst
            val items = getData(changeFirst)
            adapter.submitList(items)
        }
        return binding.root
    }

    fun getData(changeFirst: Boolean = false): ArrayList<MovieGenre> {
        val movieGenres = arrayListOf<MovieGenre>()
        var movieIdx = 0

        for (a in 1..30) {
            val movieList = arrayListOf<Movie>()
            repeat(20) {
                movieIdx++
                val movie = Movie(id = movieIdx, title = "This is movie $movieIdx", overview = "")
                movieList += movie
            }

            movieGenres += if (changeFirst && a == 1) {
                MovieGenre(id = a, name = "Genre $a", items = listOf())
            } else {
                MovieGenre(id = a, name = "Genre $a", items = movieList)
            }
        }
        return movieGenres
    }
}
