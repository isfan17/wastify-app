package com.bangkit.wastify.ui.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.wastify.ui.screens.bookmarks.SavedResultsFragment
import com.bangkit.wastify.ui.screens.bookmarks.SavedArticlesFragment

class StorageFragmentsAdapter(
    fa: FragmentActivity
): FragmentStateAdapter(fa) {

    private val fragments = listOf(
        SavedResultsFragment(),
        SavedArticlesFragment()
    )

    val fragmentNames = listOf(
        "Saved Results",
        "Saved Articles"
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}