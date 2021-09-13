package com.ahr.usergithub.ui.main

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ahr.usergithub.MainActivity
import com.ahr.usergithub.R
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.usergithub.databinding.FragmentHomeBinding
import com.ahr.usergithub.helper.MappingHelper
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivity = (activity as MainActivity)
        val adapter = SectionsPagerAdapter(childFragmentManager, lifecycle)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                getUserFromContentResolver()
                Log.d("TAG", "onChange: Call")
            }
        }
        mainActivity.contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> toSearchFragment()
            R.id.action_setting -> toSettingScreen()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserFromContentResolver() {
        GlobalScope.launch(Dispatchers.Main) {
            val differedUsers = async(Dispatchers.IO) {
                val cursor = (activity as MainActivity).contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = differedUsers.await()
            Log.d("TAG", "getUserFromContentResolver: users = $users")
        }
    }

    private fun toSearchFragment() {
        Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_searchFragment)
    }

    private fun toSettingScreen() {
        Navigation.findNavController(binding.root).navigate(R.id.action_homeFragment_to_settingPreferences)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.user_github,
            R.string.user_favorite
        )
    }
}