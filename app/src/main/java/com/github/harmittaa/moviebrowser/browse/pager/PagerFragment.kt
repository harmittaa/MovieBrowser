package com.github.harmittaa.moviebrowser.browse.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.harmittaa.moviebrowser.browse.BrowseFragment
import com.github.harmittaa.moviebrowser.databinding.FragmentPagerBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

class PagerFragment : Fragment() {
    val tabs = arrayOf("Top", "Trending", "Now Playing")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPagerBinding.inflate(inflater, container, false)

        val adapter = ScreenSlidePagerAdapter(this)
        binding.pager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }

    private inner class ScreenSlidePagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = tabs.size

        @OptIn(ExperimentalCoroutinesApi::class)
        override fun createFragment(position: Int): Fragment = BrowseFragment()
    }
}
