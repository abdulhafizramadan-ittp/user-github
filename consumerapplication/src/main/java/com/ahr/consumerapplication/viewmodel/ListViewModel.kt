package com.ahr.consumerapplication.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.consumerapplication.helper.MappingHelper
import com.ahr.consumerapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setUserFromContentProvider(context: Context, toggleLoading: (Boolean) -> Unit) {

    }

    fun getListUser(): LiveData<ArrayList<User>> = listUser
}