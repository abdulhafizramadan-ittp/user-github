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

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowViewModel::class.java)
        if (followViewModel.getListFollow().value == null) {
            followViewModel.setListFollow(activity as FragmentActivity, url as String, toggleLoading)
        }

        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollow.adapter = adapter
            rvFollow.layoutManager = LinearLayoutManager(activity)
        }

        followViewModel.getListFollow().observe(viewLifecycleOwner) { listFollow ->
            if (listFollow != null) {
                adapter.setFollow(listFollow)
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private const val ARGUMENT_URL ="arg_url"

        @JvmStatic
        fun newInstance(url: String) =
            FollowFragment().apply {
                arguments = bundleOf(ARGUMENT_URL to url)
            }
    }
}