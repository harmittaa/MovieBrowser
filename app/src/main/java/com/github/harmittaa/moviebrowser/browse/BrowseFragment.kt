package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.harmittaa.moviebrowser.browse.controllers.GenresController
import com.github.harmittaa.moviebrowser.browse.controllers.MoviesController
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BrowseFragment : Fragment() {
    private val viewModel: BrowseViewModel by viewModel()
    private val genresController: GenresController by inject()
    private val moviesController: MoviesController by inject()
    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        genresController.clickListener = viewModel
        binding.apply {
            viewModel = this@BrowseFragment.viewModel
            lifecycleOwner = this@BrowseFragment.viewLifecycleOwner
            genresRecycler.setController(genresController)
            moviesRecycler.setController(moviesController)
        }

        bindViewModel()
        binding.clearFilters.setOnClickListener {
            viewModel.clearFilters()
        }
        return binding.root
    }

    private fun bindViewModel() {
        viewModel.genres.observe(viewLifecycleOwner, { genres ->
            genresController.genres = genres
        })

        viewModel.selectedGenres.observe(viewLifecycleOwner, { genres ->
            genresController.selectedGenres = genres
        })

        viewModel.moviesOfCategory.observe(viewLifecycleOwner, { movies ->
            when (movies) {
                is Resource.Success -> moviesController.movies = movies.data
                is Resource.Error -> {
                    moviesController.movies = emptyList()
                    Toast.makeText(
                        requireContext(),
                        "Error! ${movies.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}
