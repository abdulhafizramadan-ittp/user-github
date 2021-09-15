package com.ahr.usergithub.viewmodel

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.usergithub.BuildConfig
import com.ahr.usergithub.api.GithubUserApi
import com.ahr.usergithub.model.UserDetail
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val user = MutableLiveData<UserDetail>()

    fun setUser(context: FragmentActivity,username: String) {
        GithubUserApi.instance.getUser(username).enqueue(object : Callback<UserDetail?> {
            override fun onResponse(call: Call<UserDetail?>, response: Response<UserDetail?>) {
                user.postValue(response.body())
            }

            override fun onFailure(call: Call<UserDetail?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getUser(): LiveData<UserDetail> = user
}