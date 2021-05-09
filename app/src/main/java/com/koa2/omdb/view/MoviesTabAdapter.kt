package com.koa2.omdb.view

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.koa2.omdb.view.bookmark.BookMarkFragment
import com.koa2.omdb.view.search.SearchMovieFragment

class MoviesTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return ITEM_COUNT
    }

    override fun createFragment(position: Int): Fragment = when (position) {
        SEARCH_MOVIES -> SearchMovieFragment.newInstance()
        BOOKMARK_MOVIES -> BookMarkFragment.newInstance()
        else -> throw IllegalArgumentException("Position not allowed")
    }

    companion object {
        const val OFF_SCREEN_LIMIT = 1
        const val ITEM_COUNT = 2

        const val SEARCH_MOVIES = 0
        const val BOOKMARK_MOVIES = 1
    }
}