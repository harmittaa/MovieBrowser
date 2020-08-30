package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import com.github.harmittaa.moviebrowser.domain.Movie
import com.github.harmittaa.moviebrowser.domain.MovieGenreLocal
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
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
            viewModel.getMovieCategories()
        }

        bindViewModel()

        return binding.root
    }

    private fun bindViewModel() {
        viewModel.genres.observe(viewLifecycleOwner, { genres ->
            when (genres) {
                is Resource.Success -> {
                    updateListWithCorrectNames(genres.data)
                }
            }
        })
    }

    private fun updateListWithCorrectNames(genres: List<MovieGenreLocal>) {
        var movieIdx = 0

        genres.forEach {
            val movieList = arrayListOf<Movie>()
            repeat(20) {
                movieIdx++
                val movie = Movie(id = movieIdx, title = "This is movie $movieIdx", overview = "")
                movieList += movie
            }
            it.items = movieList
        }
        adapter.submitList(genres)
    }

    fun getData(changeFirst: Boolean = false): ArrayList<MovieGenreLocal> {
        val movieGenres = arrayListOf<MovieGenreLocal>()
        var movieIdx = 0

        for (a in 1..30) {
            val movieList = arrayListOf<Movie>()
            repeat(20) {
                movieIdx++
                val movie = Movie(id = movieIdx, title = "This is movie $movieIdx", overview = "")
                movieList += movie
            }

            movieGenres += if (changeFirst && a == 1) {
                MovieGenreLocal(id = a, name = "Genre $a", items = listOf())
            } else {
                MovieGenreLocal(id = a, name = "Genre $a", items = movieList)
            }
        }
        return movieGenres
    }
}
