package com.koa2.omdb.view.bookmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.koa2.omdb.AppConstants
import com.koa2.omdb.databinding.FragmentBookmarkBinding
import com.koa2.omdb.room.MovieBookMarkData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookMarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val bookMarkViewModel: BookMarkViewModel by viewModels()
    private val adapter = BookMarkMoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        Log.d(TAG, "on Create View")
        binding = FragmentBookmarkBinding.inflate(LayoutInflater.from(context))
        binding.bookmarksList.layoutManager = GridLayoutManager(context, 2)
        binding.bookmarksList.adapter = adapter

        initListeners()
        return binding.root
    }

    private fun initListeners() {
        bookMarkViewModel.bookMarksData.observe(viewLifecycleOwner, {
            Log.d(TAG, "ON BookMarks updated  : ${it.size}")
            updateUI(it)
        })
    }

    private fun updateUI(dataList: List<MovieBookMarkData>) {
        if (dataList.isEmpty()) {
            binding.bookmarksEmpty.visibility = View.VISIBLE
        } else {
            binding.bookmarksEmpty.visibility = View.GONE
        }
        adapter.updateList(dataList)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "On Resume")
    }


    companion object {
        const val TAG = AppConstants.TAG.plus("-BookMarkFrag")
        fun newInstance() = BookMarkFragment()
    }
}