package com.ahr.usergithub.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FollowsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val urls = arrayListOf<String>()

    override fun getItemCount(): Int = urls.size

    override fun createFragment(position: Int): Fragment = FollowFragment.newInstance(urls[position])

    fun setUrl(url: String) {
        urls.add(url)
    }
}