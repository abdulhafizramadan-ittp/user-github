package com.ahr.usergithub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String = "",
    var avatar: String = "",
    var apiUser: String = "",
    var apiFollower: String = "",
    var apiFollowing: String = ""
) : Parcelable