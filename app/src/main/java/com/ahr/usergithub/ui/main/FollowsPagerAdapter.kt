package com.ahr.usergithub.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahr.usergithub.ui.main.DetailFragment.Companion.TAB_TITLE

class FollowsPagerAdapter(private val context: Context, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val urls = arrayListOf<String>()

    override fun getItemCount(): Int = urls.size

    override fun createFragment(position: Int): Fragment = FollowFragment.newInstance(context.getString(TAB_TITLE[position]), urls[position])

    fun setUrl(url: String) {
        urls.add(url)
    }
}