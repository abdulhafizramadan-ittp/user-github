package com.ahr.consumerapplication

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahr.consumerapplication.adapter.ListAdapter
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.consumerapplication.databinding.ActivityMainBinding
import com.ahr.consumerapplication.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
            rvUsers.setHasFixedSize(true)
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

        loadUserFromContentProvider()
    }

    private fun loadUserFromContentProvider() {
        GlobalScope.launch(Dispatchers.Main) {
            val differedUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = differedUser.await()
            Log.d("MainActivity", "loadUserFromContentProvider: notes = $users")
            adapter.setListUser(users)
        }

    }
}