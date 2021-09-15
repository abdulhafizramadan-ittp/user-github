package com.ahr.usergithub.model

import com.google.gson.annotations.SerializedName

data class ResponseSearchUser(
    @SerializedName("items") val listUsers: ArrayList<User>?
)
