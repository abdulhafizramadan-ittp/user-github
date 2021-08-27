package com.example.usergithub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.usergithub.databinding.FragmentDetailBinding
import com.example.usergithub.model.UserDetail
import com.example.usergithub.ui.main.SectionsPagerAdapter
import com.example.usergithub.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (_, _, apiUser, apiFollower, apiFollowing) = DetailFragmentArgs.fromBundle(arguments as Bundle).user

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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}