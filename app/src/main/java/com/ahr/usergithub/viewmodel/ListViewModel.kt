package com.ahr.usergithub.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.usergithub.api.GithubUserApi
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.usergithub.helper.MappingHelper
import com.ahr.usergithub.model.ResponseSearchUser
import com.ahr.usergithub.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel : ViewModel() {

    private var listUser = MutableLiveData<ArrayList<User>?>()

    fun setUserFromApi(context: Context) {
        GithubUserApi.instance.getUser().enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(call: Call<ArrayList<User>?>, response: Response<ArrayList<User>>) {
                listUser.postValue(response.body())
            }

            override fun onFailure(call: Call<ArrayList<User>?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                listUser.postValue(null)
            }
        })
    }

    fun setUserFromApi(context: Context, username: String) {
        GithubUserApi.instance.getUserByUsername(username).enqueue(object : Callback<ResponseSearchUser?> {
            override fun onResponse(call: Call<ResponseSearchUser?>, response: Response<ResponseSearchUser?>) {
                listUser.postValue(response.body()?.listUsers)
            }

            override fun onFailure(call: Call<ResponseSearchUser?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                listUser.postValue(null)
            }
        })
    }

    fun setUserFromLocal(context: Context) {
        val cursor = context.contentResolver?.query(CONTENT_URI, null, null, null, null)
        val users = MappingHelper.mapCursorToArrayList(cursor)
        listUser.postValue(users)
    }

    fun getListUser(): LiveData<ArrayList<User>?> = listUser
}