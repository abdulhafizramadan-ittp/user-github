package com.ahr.usergithub

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.adapter.ListAdapter
import com.ahr.usergithub.databinding.FragmentListBinding
import com.ahr.usergithub.model.User
import com.ahr.usergithub.viewmodel.ListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListAdapter
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.list_menu)
            setSearchAction(menu)
        }


        listViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ListViewModel::class.java)
        if (listViewModel.getListUser().value == null) {
            listViewModel.setListUser(activity as FragmentActivity, toggleLoading)
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

    private fun setSearchAction(menu: Menu?) {
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            queryHint = getString(R.string.search_hint)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listViewModel.setListUser(activity as Context, query as String, toggleLoading)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    fun toDetailFragment(user: User) {
        val toDetailFragment = ListFragmentDirections.actionListFragmentToDetailFragment(user)
        Navigation.findNavController(binding.rvUsers).navigate(toDetailFragment)
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
}