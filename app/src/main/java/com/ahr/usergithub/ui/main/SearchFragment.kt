package com.ahr.usergithub.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.MainActivity
import com.ahr.usergithub.adapter.ListAdapter
import com.ahr.usergithub.databinding.FragmentSearchBinding
import com.ahr.usergithub.model.User
import com.ahr.usergithub.viewmodel.ListViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var username = ""

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

        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//        inputMethodManager.showSoftInput(binding.tieSearch, InputMethodManager.SHOW_IMPLICIT)

        binding.tieSearch.apply {
            requestFocus()
            setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER)
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == KeyEvent.KEYCODE_ENTER && username.isNotEmpty()) {
                    listViewModel.setListUser(activity as FragmentActivity, username, toggleLoading)

                }
                true
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    username = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toDetailFragment(user: User) {
        val  toDetailFragment = SearchFragmentDirections.actionSearchFragmentToDetailFragment(user)
        Navigation.findNavController(binding.root).navigate(toDetailFragment)
    }

    private val toggleLoading: (Boolean) -> Unit = { state: Boolean ->
        when(state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            else -> binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}