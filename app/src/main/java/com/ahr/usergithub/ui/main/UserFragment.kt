package com.ahr.usergithub.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.adapter.ListAdapter
import com.ahr.usergithub.databinding.FragmentUserBinding
import com.ahr.usergithub.model.User
import com.ahr.usergithub.viewmodel.ListViewModel

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString(ARGUMENT_TYPE)

        listViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ListViewModel::class.java)

        if (listViewModel.getListUser().value == null) {
            if (type == HOME_TYPES[0]) {
                listViewModel.setUserFromApi(activity as FragmentActivity, toggleLoading)
            }
        }

        if (type == HOME_TYPES[1]) {
            listViewModel.setUserFromLocal(activity as FragmentActivity, toggleLoading)
        }

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                toDetailFragment(user)
            }
        })

        binding.apply {
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(activity)
        }

        listViewModel.getListUser().observe(viewLifecycleOwner) { listUser ->
            if (listUser != null) {
                adapter.setListUser(listUser)
            }
        }
    }

    fun toDetailFragment(user: User) {
        val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment(user)
        Navigation.findNavController(binding.root).navigate(toDetailFragment)
    }

    private val toggleLoading: (Boolean) -> Unit = { state: Boolean ->
        when(state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            else -> binding.progressBar.visibility = View.INVISIBLE
        }
    }

    companion object {

        private const val TAG = "UserFragment"
        private const val ARGUMENT_TYPE = "argument_type"

        val HOME_TYPES = arrayOf(
            "from_api",
            "from_local"
        )

        @JvmStatic
        fun newInstance(type: String) =
            UserFragment().apply {
                arguments = bundleOf(ARGUMENT_TYPE to type)
            }
    }
}