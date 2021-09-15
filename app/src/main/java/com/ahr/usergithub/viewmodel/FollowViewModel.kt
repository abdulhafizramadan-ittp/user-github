package com.ahr.usergithub.viewmodel

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.usergithub.BuildConfig
import com.ahr.usergithub.api.GithubUserApi
import com.ahr.usergithub.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val listFollow = MutableLiveData<ArrayList<User>?>()
    
    fun setListFollow(context: FragmentActivity, url: String) {
        GithubUserApi.instance.getUserFollow(url).enqueue(object : Callback<ArrayList<User>?> {
            override fun onResponse(call: Call<ArrayList<User>?>, response: Response<ArrayList<User>?>) {
                listFollow.postValue(response.body())
            }

            override fun onFailure(call: Call<ArrayList<User>?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                listFollow.postValue(null)
            }
        })
    }
    
    fun getListFollow(): LiveData<ArrayList<User>?> = listFollow
}