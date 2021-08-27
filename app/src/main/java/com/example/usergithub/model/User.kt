package com.example.usergithub.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val avatar: String,
    val apiUser: String,
    val apiFollower: String,
    val apiFollowing: String
) : Parcelable