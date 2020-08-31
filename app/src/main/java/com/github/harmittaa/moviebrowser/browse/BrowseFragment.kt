package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.harmittaa.moviebrowser.browse.genres.GenreAdapter
import com.github.harmittaa.moviebrowser.browse.genres.GenreItemDecorator
import com.github.harmittaa.moviebrowser.browse.movies.MovieBrowseAdapter
import com.github.harmittaa.moviebrowser.browse.movies.MovieItemDecorator
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class BrowseFragment : Fragment() {
    val viewModel: BrowseViewModel by viewModel()
    lateinit var movieBrowseAdapter: MovieBrowseAdapter
    lateinit var genreAdapter: GenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBrowseBinding.inflate(inflater, container, false)
        movieBrowseAdapter = MovieBrowseAdapter(viewModel)
        genreAdapter = GenreAdapter(viewModel)
        binding.apply {
            viewModel = this@BrowseFragment.viewModel
            lifecycleOwner = this@BrowseFragment.viewLifecycleOwner
            browseRecycler.adapter = this@BrowseFragment.movieBrowseAdapter
            browseRecycler.addItemDecoration(MovieItemDecorator())
            genresRecycler.adapter = this@BrowseFragment.genreAdapter
            genresRecycler.addItemDecoration(GenreItemDecorator())
        }

        bindViewModel()
        return binding.root
    }

    private fun bindViewModel() {
        viewModel.genres.observe(viewLifecycleOwner, { genres ->
            when (genres) {
                is Resource.Success -> {
                    genreAdapter.submitList(genres.data)
                }
            }
        })

        viewModel.moviesOfCategory.observe(viewLifecycleOwner, { movies ->
            if (movies is Resource.Success) {
                lifecycleScope.launch {
                    Timber.d("viewModel.moviesOfCategory.observe!")
                    movieBrowseAdapter.submitList(movies.data)
                    viewModel.listRefreshed()
                }
            }
        })

        viewModel.selectedGenre.observe(viewLifecycleOwner, { genrePosition ->
            browse_recycler.smoothScrollToPosition(genrePosition)
        })
    }
}
