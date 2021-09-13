package com.ahr.consumerapplication.model

data class UserDetail(
    val avatar: String,
    val name: String,
    val description: String,
    val repository: Int,
    val follower: Int,
    val following: Int,
    val gists: Int,
)
