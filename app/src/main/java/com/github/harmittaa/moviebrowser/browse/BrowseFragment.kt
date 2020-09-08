package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyController
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import com.github.harmittaa.moviebrowser.epoxy.GenresController
import com.github.harmittaa.moviebrowser.epoxy.MoviesController
import com.github.harmittaa.moviebrowser.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class BrowseFragment : Fragment() {
    private val viewModel: BrowseViewModel by viewModel()
    private lateinit var genresController: GenresController
    private lateinit var moviesController: MoviesController
    private lateinit var binding: FragmentBrowseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseBinding.inflate(inflater, container, false)
        genresController = GenresController(viewModel)
        moviesController = MoviesController(viewModel)
        binding.apply {
            viewModel = this@BrowseFragment.viewModel
            lifecycleOwner = this@BrowseFragment.viewLifecycleOwner
            genresRecycler.setController(genresController)
            moviesRecycler.setController(moviesController)
        }

        bindViewModel()
        viewModel.onCreateView()
        return binding.root
    }

    private fun bindViewModel() {
        viewModel.genres.observe(viewLifecycleOwner, { genres ->
            when (genres) {
                is Resource.Success -> {
                    // genresController.genres = genres.data
                    // genreAdapter.submitList(genres.data)
                    // genresController.genres = genres.data
                }
            }
        })

        viewModel.moviesOfCategory.observe(viewLifecycleOwner, { movies ->
            if (movies is Resource.Success) {
                Timber.d("viewModel.moviesOfCategory.observe!")

                val handlerThread = HandlerThread("epoxy")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                EpoxyController.defaultDiffingHandler = handler
                EpoxyController.defaultModelBuildingHandler = handler

                viewModel.listRefreshed()
                moviesController.genres = movies.data
                genresController.genres = movies.data
            }
        })

        viewModel.selectedGenre.observe(viewLifecycleOwner, { genrePosition ->
            binding.genresRecycler.smoothScrollToPosition(genrePosition)
            // browse_recycler.smoothScrollToPosition(genrePosition)
        })
    }
}
