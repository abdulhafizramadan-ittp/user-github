package com.ahr.usergithub.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("login") var username: String = "",
    @SerializedName("avatar_url") var avatar: String = "",
    @SerializedName("url") var apiUser: String = "",
    @SerializedName("followers_url") var apiFollower: String = "",
    @SerializedName("following_url") var apiFollowing: String = ""
) : Parcelable