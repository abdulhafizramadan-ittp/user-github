package com.ahr.usergithub.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.usergithub.MainActivity
import com.ahr.usergithub.R
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
        (activity as MainActivity).setSupportActionBar(binding.includeAppbar.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listViewModel = ViewModelProvider(activity as ViewModelStoreOwner, ViewModelProvider.NewInstanceFactory()).get(ListViewModel::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            Navigation.findNavController(binding.root).navigate(R.id.action_navigation_home_to_searchFragment)
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun setSearchAction(menu: Menu?) {
//        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
//
//        searchView.apply {
//            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
//            queryHint = getString(R.string.search_hint)
//        }
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                listViewModel.setListUser(activity as Context, query as String, toggleLoading)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                return false
//            }
//        })
//    }

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

    companion object {
        private const val TAG = "ListFragment"
    }
}