package com.ahr.usergithub.viewmodel

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahr.usergithub.BuildConfig
import com.ahr.usergithub.model.UserDetail
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel : ViewModel(){
    private val user = MutableLiveData<UserDetail>()

    fun setUser(context: FragmentActivity,url: String, toggleLoading: (Boolean) -> Unit) {
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
                    val jsonObject = JSONObject(result)

                    val name = jsonObject.getString("name").let {
                        if (it == "null") {
                            jsonObject.getString("login")
                        } else {
                            it
                        }
                    }
                    val description = jsonObject.getString("bio").let {
                        if (it == "null") {
                            "-"
                        } else {
                            it
                        }
                    }

                    jsonObject.apply {
                        user.postValue(UserDetail(
                            getString("avatar_url"),
                            name,
                            description,
                            getInt("public_repos"),
                            getInt("followers"),
                            getInt("following"),
                            getInt("public_gists")
                        ))
                    }

                    toggleLoading(false)
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

    fun getUser(): LiveData<UserDetail> = user
}