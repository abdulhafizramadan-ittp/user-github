package com.ahr.usergithub.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahr.usergithub.ui.main.UserFragment.Companion.HOME_TYPES

class SectionsPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = HOME_TYPES.size

    override fun createFragment(position: Int): Fragment = UserFragment.newInstance(HOME_TYPES[position])
}