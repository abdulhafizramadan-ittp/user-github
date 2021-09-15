package com.ahr.usergithub.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.adapter.FollowAdapter
import com.ahr.usergithub.databinding.FragmentFollowBinding
import com.ahr.usergithub.viewmodel.FollowViewModel
import java.util.*

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FollowAdapter
    private lateinit var followViewModel: FollowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = (arguments?.getString(ARGUMENT_TYPE) as String).lowercase(Locale.getDefault())
        val url = when (type) {
            "following" -> (arguments?.getString(ARGUMENT_URL) as String).dropLast(13)
            else -> arguments?.getString(ARGUMENT_URL) as String
        }

        Log.d("FollowFragment", "onViewCreated: type = $type\nurl = $url")

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)
        if (followViewModel.getListFollow().value == null) {
            toggleLoading(true)
            followViewModel.setListFollow(activity as FragmentActivity, url)
        }

        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollow.adapter = adapter
            rvFollow.layoutManager = LinearLayoutManager(activity)
        }

        followViewModel.getListFollow().observe(viewLifecycleOwner) { listFollow ->
            when (listFollow) {
                null -> {
                    toggleLoading(false)
                    toggleNotFound(message = "No internet connection", state = true)
                }
                else -> {
                    toggleLoading(false)
                    when {
                        listFollow.size > 0 -> adapter.setFollow(listFollow)
                        else -> toggleNotFound(message = "No $type", state = true)
                    }
                }
            }
        }
    }

    private fun toggleNotFound(message: String = "", state: Boolean) {
        when (state) {
            true -> {
                binding.apply {
                    lottieNotFound.visibility = View.VISIBLE
                    tvNoData.apply {
                        text = message
                        visibility = View.VISIBLE
                    }
                }
            }
            else -> {
                binding.apply {
                    lottieNotFound.visibility = View.INVISIBLE
                    tvNoData.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val toggleLoading: (Boolean) -> Unit = { state ->
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARGUMENT_TYPE = "arg_type"
        private const val ARGUMENT_URL ="arg_url"

        @JvmStatic
        fun newInstance(type: String, url: String) =
            FollowFragment().apply {
                arguments = bundleOf(
                    ARGUMENT_TYPE to type,
                    ARGUMENT_URL to url
                )
            }
    }
}