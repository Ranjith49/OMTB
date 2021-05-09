package com.koa2.omdb.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.koa2.omdb.AppConstants
import com.koa2.omdb.OMDBApplication
import com.koa2.omdb.R
import com.koa2.omdb.databinding.ActivityMainBinding
import com.koa2.omdb.view.detail.DetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private val moviesViewModel: MoviesViewModel by viewModels()
    private var selectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        tabLayout = binding.tabs
        viewPager = binding.viewPager
        setupViewPager()
        initListeners()
    }

    private fun initListeners() {
        moviesViewModel.movieClickLiveData.observe(this, {
            Log.d(TAG, "On Movie Click : ${it.movieId}")
            val detailFragment = DetailFragment.newInstance(it.movieId)
            detailFragment.show(supportFragmentManager, DETAIL_FRAGMENT_TAG)
        })
    }

    @SuppressLint("WrongConstant")
    private fun setupViewPager() {
        val moviesAdapter = MoviesTabAdapter(this)
        viewPager.currentItem = selectedPosition
        viewPager.apply {
            adapter = moviesAdapter
            offscreenPageLimit = MoviesTabAdapter.OFF_SCREEN_LIMIT
        }

        // Attach view pager to tabLayout..
        TabLayoutMediator(tabLayout, viewPager) { currentTab, position ->
            currentTab.text = when (position) {
                MoviesTabAdapter.SEARCH_MOVIES -> getString(R.string.search_tab)
                MoviesTabAdapter.BOOKMARK_MOVIES -> getString(R.string.bookmark_tab)
                else -> getString(R.string.unknown_tab)
            }
        }.attach()
    }

    companion object {
        const val TAG = AppConstants.TAG.plus("-MoviesActivity")
        const val DETAIL_FRAGMENT_TAG = "DetailFragment-TAG"
    }
}