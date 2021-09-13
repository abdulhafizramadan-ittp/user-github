package com.ahr.consumerapplication

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.consumerapplication.adapter.ListAdapter
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.consumerapplication.databinding.ActivityMainBinding
import com.ahr.consumerapplication.viewmodel.ListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ListViewModel::class.java)

        if (listViewModel.getListUser().value == null) {
            listViewModel.setUserFromLocal(this)
        }

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUsers.setHasFixedSize(true)
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                listViewModel.setUserFromLocal(this@MainActivity)
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        listViewModel.getListUser().observe(this) { listUser ->
            if (listUser != null) {
                adapter.setListUser(listUser)
            }
        }
    }
}