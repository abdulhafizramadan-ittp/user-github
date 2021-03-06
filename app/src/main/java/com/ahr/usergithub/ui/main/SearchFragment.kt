package com.ahr.usergithub.ui.main

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.MainActivity
import com.ahr.usergithub.R
import com.ahr.usergithub.adapter.ListAdapter
import com.ahr.usergithub.databinding.FragmentSearchBinding
import com.ahr.usergithub.model.User
import com.ahr.usergithub.viewmodel.ListViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputMethodManager: InputMethodManager

    private lateinit var adapter: ListAdapter
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbarSearch)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel = ViewModelProvider(this as ViewModelStoreOwner, ViewModelProvider.NewInstanceFactory()).get(ListViewModel::class.java)

        inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (listViewModel.getListUser().value == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                toggleKeyboard(true)
            }, 500)
        }

        binding.tieSearch.apply {
            requestFocus()
            setImeActionLabel(getString(R.string.search), 0)
            setOnEditorActionListener { _, _, _ ->
                if (binding.tieSearch.text.toString().isNotEmpty()) {
                    toggleLoading(true)
                    listViewModel.setUserFromApi(activity as FragmentActivity, binding.tieSearch.text.toString())
                    toggleKeyboard(false)
                }
                true
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
                    toggleNotFound(message = "No internet connection", state = true)
                }
                else -> {
                    toggleLoading(false)
                    when {
                        listUser.size > 0 -> adapter.setListUser(listUser)
                        else -> {
                            toggleRvUser(false)
                            toggleNotFound(message = "User not found", state = true)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            toggleKeyboard(false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toDetailFragment(user: User) {
        val  toDetailFragment = SearchFragmentDirections.actionSearchFragmentToDetailFragment(user)
        Navigation.findNavController(binding.root).navigate(toDetailFragment)
    }

    private fun toggleRvUser(state: Boolean) {
        binding.rvUsers.visibility = when (state) {
            true -> View.VISIBLE
            else -> View.INVISIBLE
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

    private fun toggleKeyboard(state: Boolean) {
        when (state) {
            true -> inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            else -> inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        }

    }

    private val toggleLoading: (Boolean) -> Unit = { state: Boolean ->
        when(state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            else -> binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}