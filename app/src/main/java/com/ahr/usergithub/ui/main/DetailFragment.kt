package com.ahr.usergithub.ui.main

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ahr.usergithub.R
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWING
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_USER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.AVATAR
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.ahr.usergithub.database.UserHelper
import com.ahr.usergithub.databinding.FragmentDetailBinding
import com.ahr.usergithub.model.User
import com.bumptech.glide.Glide
import com.ahr.usergithub.model.UserDetail
import com.ahr.usergithub.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var userHelper: UserHelper
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userHelper = UserHelper.getInstance(activity?.applicationContext as Context)
        userHelper.open()

        val (username, avatar, apiUser, apiFollower, apiFollowing) = DetailFragmentArgs.fromBundle(arguments as Bundle).user

        checkIsFavorite(username)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        if (detailViewModel.getUser().value == null) {
            detailViewModel.setUser(activity as FragmentActivity, apiUser, toggleLoading)
        }

        detailViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                setupUserDetail(user)
            }
        }

        val adapter = SectionsPagerAdapter(childFragmentManager, lifecycle).apply {
            setUrl(apiFollower)
            setUrl(apiFollowing)
        }

        binding.viewPager.adapter = adapter

        binding.includeUserDetail.fabFavorite.apply {
            setOnClickListener {
                if (!isFavorite) {
                    val result = userHelper.insert(contentValuesOf(
                        USERNAME to username,
                        AVATAR to avatar,
                        API_USER to apiUser,
                        API_FOLLOWER to apiFollower,
                        API_FOLLOWING to apiFollowing
                    ))
                    if (result > 0) {
                        toggleFavorite()
                    }
                    Toast.makeText(activity as Context, "result = $result", Toast.LENGTH_SHORT).show()
                } else {
                    val result = userHelper.deleteByUsername(username)
                    if (result > 0) {
                        toggleFavorite()
                    }
                    Toast.makeText(activity as Context, "result = result", Toast.LENGTH_SHORT).show()
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

    }

    private fun setupUserDetail(user: UserDetail) {
        binding.includeUserDetail.apply {
            Glide.with(this@DetailFragment)
                .load(user.avatar)
                .into(ivDetailPhoto)

            tvName.text = user.name
            tvDescription.text = user.description
            tvRepository.text = user.repository.toString()
            tvFollower.text = user.follower.toString()
            tvFollowing.text = user.following.toString()
            tvGists.text = user.gists.toString()
        }
    }

    private fun checkIsFavorite(username: String) {
        val result = userHelper.queryByUsername(username).moveToFirst()
        if (result) {
            toggleFavorite()
        }
    }

    private fun toggleFavorite() {
        isFavorite = !isFavorite
        binding.includeUserDetail.fabFavorite.apply {
            if (isFavorite) {
                setImageDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.ic_favorite))
            } else {
                setImageDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.ic_unfavorite))
            }
        }
    }

    private val toggleLoading: (Boolean) -> Unit =  { state ->
        when (state) {
            true -> {
                binding.apply {
                    appbar.visibility = View.INVISIBLE
                    nestedScroll.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
            }
            else -> {
                binding.apply {
                    appbar.visibility = View.VISIBLE
                    nestedScroll.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        userHelper.close()
    }

    companion object {
        private const val TAG = "DetailFragment"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}