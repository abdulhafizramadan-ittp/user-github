package com.ahr.consumerapplication

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.consumerapplication.adapter.ListAdapter
import com.ahr.consumerapplication.database.DatabaseContract
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.consumerapplication.databinding.ActivityMainBinding
import com.ahr.consumerapplication.helper.MappingHelper
import com.ahr.consumerapplication.viewmodel.ListViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()

        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserFromContentProvider()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun loadUserFromContentProvider() {
        val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
        val notes = MappingHelper.mapCursorToArrayList(cursor)
        adapter.setListUser(notes)
    }
}