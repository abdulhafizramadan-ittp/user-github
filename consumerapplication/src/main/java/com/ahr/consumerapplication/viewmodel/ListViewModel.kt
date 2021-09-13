package com.ahr.consumerapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.consumerapplication.helper.MappingHelper
import com.ahr.consumerapplication.model.User

class ListViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setUserFromLocal(context: Context) {
        val cursor = context.contentResolver?.query(CONTENT_URI, null, null, null, null)
        val users = MappingHelper.mapCursorToArrayList(cursor)
        listUser.postValue(users)
    }

    fun getListUser(): LiveData<ArrayList<User>> = listUser
}