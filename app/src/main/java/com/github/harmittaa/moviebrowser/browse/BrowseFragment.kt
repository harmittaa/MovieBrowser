package com.github.harmittaa.moviebrowser.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.harmittaa.moviebrowser.databinding.FragmentBrowseBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class BrowseFragment : Fragment() {
    val viewModel: BrowseViewModel by viewModel()
    lateinit var adapter: MovieGenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentBrowseBinding.inflate(inflater, container, false)
        adapter = MovieGenreAdapter(viewModel)
        binding.apply {
            viewModel = this@BrowseFragment.viewModel
            lifecycleOwner = this@BrowseFragment.viewLifecycleOwner
            browseRecycler.adapter = this@BrowseFragment.adapter
        }

        bindViewModel()
        return binding.root
    }

    private fun bindViewModel() {
        viewModel.moviesOfCategory.observe(viewLifecycleOwner, { movies ->
            adapter.submitList(movies.data)
        })
    }
}
