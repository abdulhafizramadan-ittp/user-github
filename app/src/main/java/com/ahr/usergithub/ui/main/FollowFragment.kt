package com.ahr.usergithub.ui.main

import android.os.Bundle
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

        val url = arguments?.getString(ARGUMENT_URL)
        val type = (arguments?.getString(ARGUMENT_TYPE) as String).lowercase(Locale.getDefault())

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)
        if (followViewModel.getListFollow().value == null) {
            toggleLoading(true)
            followViewModel.setListFollow(activity as FragmentActivity, url as String)
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
                    when {
                        listFollow.size > 0 -> {
                            toggleLoading(false)
                            toggleNotFound(state = false)
                            adapter.setFollow(listFollow)
                        }
                        else -> {
                            toggleLoading(false)
                            toggleNotFound(message = "No $type", state = true)
                        }
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