package com.bangkit.wastify.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.wastify.ui.screens.history.IdentificationsFragment
import com.bangkit.wastify.ui.screens.history.SavedArticlesFragment

class HistoryFragmentsAdapter(
    fa: FragmentActivity
): FragmentStateAdapter(fa) {

    private val fragments = listOf<Fragment>(
        IdentificationsFragment(),
        SavedArticlesFragment()
    )

    val fragmentNames = listOf<String>(
        "Identifications",
        "Saved Articles"
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}