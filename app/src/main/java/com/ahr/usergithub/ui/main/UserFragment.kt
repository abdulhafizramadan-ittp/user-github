package com.ahr.usergithub.ui.main

import android.content.Context
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.adapter.ListAdapter
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
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

        when (type) {
            HOME_TYPES[0] -> {
                if (listViewModel.getListUser().value == null) {
                    listViewModel.setUserFromApi(context as Context)
                    toggleLoading(true)
                }
            }
            HOME_TYPES[1] -> {
                if (listViewModel.getListUser().value == null) {
                    listViewModel.setUserFromLocal(context as Context)
                }
                val handlerThread = HandlerThread("DataObserver").apply {
                    start()
                }
                val handler = Handler(handlerThread.looper)

                val myObserver = object : ContentObserver(handler) {
                    override fun onChange(selfChange: Boolean) {
                        listViewModel.setUserFromLocal(context as Context)
                    }
                }
                context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
            }
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
            when (listUser) {
                null -> {
                    toggleLoading(false)
                    toggleNoData("No internet connection", true)
                }
                else -> {
                    when {
                        listUser.size > 0 -> {
                            toggleLoading(false)
                            toggleNoData("", false)
                            adapter.setListUser(listUser)
                        }
                        else -> {
                            toggleNoData("No user favorite", true)
                        }
                    }
                }
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

    private fun toggleNoData(message: String, state: Boolean) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

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