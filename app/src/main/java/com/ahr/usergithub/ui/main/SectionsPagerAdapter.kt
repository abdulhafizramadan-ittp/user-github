package com.ahr.usergithub.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val types = arrayOf(
        "from_api",
        "from_local"
    )

    override fun getItemCount(): Int = types.size

    override fun createFragment(position: Int): Fragment = UserFragment.newInstance(types[position])
}