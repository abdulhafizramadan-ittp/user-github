package com.example.usergithub.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.usergithub.BuildConfig
import com.example.usergithub.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class ListViewModel : ViewModel() {
    private val listUser = MutableLiveData<ArrayList<User>>()

    fun setListUser(context: FragmentActivity, toggleLoading: (Boolean) -> Unit) {
        toggleLoading(true)

        val token = BuildConfig.GITHUB_TOKEN
        val url = "https://api.github.com/users"

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
                    listUser.postValue(list)
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

    fun setListUser(context: Context, username: String, toggleLoading: (Boolean) -> Unit) {
        toggleLoading(true)

        val token = BuildConfig.GITHUB_TOKEN
        val url = "https://api.github.com/search/users?q=$username"

        val client = AsyncHttpClient().apply {
            addHeader("Authorization", token)
            addHeader("User-Agent", "request")
        }

        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val result = String(responseBody as ByteArray)
                    val jsonObject= JSONObject(result)
                    val users = jsonObject.getJSONArray("items")
                    val list = ArrayList<User>()

                    for (i in 0 until  users.length()) {
                        users.getJSONObject(i).apply {
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
                    listUser.postValue(list)
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

    fun getListUser(): LiveData<ArrayList<User>> = listUser
}