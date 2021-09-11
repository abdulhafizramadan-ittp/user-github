package com.ahr.usergithub.viewmodel

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.usergithub.BuildConfig
import com.ahr.usergithub.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowViewModel : ViewModel() {
    private val listFollow = MutableLiveData<ArrayList<User>>()
    
    fun setListFollow(context: FragmentActivity, url: String, toggleLoading: (Boolean) -> Unit) {
        toggleLoading(true)

        val token = BuildConfig.GITHUB_TOKEN

        val client = AsyncHttpClient().apply {
            addHeader("Authorization", token)
            addHeader("User-Agent", "request")
        }

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody as ByteArray)
                    val jsonArray = JSONArray(result)

                    val list = ArrayList<User>()
                    for (i in 0 until  jsonArray.length()) {
                        jsonArray.getJSONObject(i).apply {
                            val user = User(
                                getString("login"),
                                getString("avatar_url"),
                                getString("url"),
                                getString("followers_url"),
                                getString("following_url").dropLast(13)
                            )
                            list.add(user)
                        }
                    }

                    toggleLoading(false)
                    listFollow.postValue(list)
                } catch (err: Exception) {
                    toggleLoading(false)
                    Toast.makeText(context, err.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                toggleLoading(false)
                Toast.makeText(context, error?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    fun getListFollow(): LiveData<ArrayList<User>> = listFollow
}