package com.ahr.usergithub.ui.main

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.ahr.usergithub.R
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWING
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_USER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.AVATAR
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.ahr.usergithub.databinding.FragmentDetailBinding
import com.ahr.usergithub.model.UserDetail
import com.ahr.usergithub.viewmodel.DetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var userUri: Uri

    private lateinit var detailViewModel: DetailViewModel
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

        val (username, avatar, apiUser, apiFollower, apiFollowing) = DetailFragmentArgs.fromBundle(arguments as Bundle).user

        userUri = Uri.parse("$CONTENT_URI/$username")
        checkIsFavorite()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)
        if (detailViewModel.getUser().value == null) {
            toggleLoading(true)
            detailViewModel.setUser(activity as FragmentActivity, username)
        }

        detailViewModel.getUser().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                toggleLoading(false)
                setupUserDetail(user)
            }
        }

        val adapter = FollowsPagerAdapter(context as Context, childFragmentManager, lifecycle).apply {
            setUrl(apiFollower)
            setUrl(apiFollowing)
        }

        binding.viewPager.adapter = adapter

        binding.includeUserDetail.fabFavorite.apply {
            setOnClickListener {
                if (!isFavorite) {
                    val result = context?.contentResolver?.insert(CONTENT_URI, contentValuesOf(
                        USERNAME to username,
                        AVATAR to avatar,
                        API_USER to apiUser,
                        API_FOLLOWER to apiFollower,
                        API_FOLLOWING to apiFollowing
                    ))
                    if (result?.lastPathSegment?.toInt() as Int > 0) {
                        toggleFavorite()
                        Toast.makeText(context as Context, "$username added to favorite", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val result = context?.contentResolver?.delete(userUri, null, null) as Int
                    if (result > 0) {
                        toggleFavorite()
                        Toast.makeText(context as Context, "$username remove from favorite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(TAB_TITLE[position])
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

    private fun checkIsFavorite() {
        val result = context?.contentResolver?.query(userUri, null, null, null, null)
        if (result?.moveToNext() as Boolean) {
            toggleFavorite()
        }
        result.close()
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
                    viewPager.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
            }
            else -> {
                binding.apply {
                    appbar.visibility = View.VISIBLE
                    viewPager.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        val TAB_TITLE = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}