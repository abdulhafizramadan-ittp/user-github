package com.ahr.consumerapplication.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.consumerapplication.model.User

class ListViewModel : ViewModel() {

    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setUserFromContentProvider(context: Context, toggleLoading: (Boolean) -> Unit) {

    }

    fun getListUser(): LiveData<ArrayList<User>> = listUser
}