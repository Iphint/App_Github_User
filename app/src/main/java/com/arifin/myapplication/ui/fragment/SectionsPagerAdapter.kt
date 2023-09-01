package com.arifin.myapplication.ui.fragment

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(
    private val activity: AppCompatActivity,
    private val username: String,
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment.newInstance(username)
            1 -> FollowingFragment.newInstance(username)
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
