package com.ahr.usergithub.database

import android.provider.BaseColumns

internal object DatabaseContract {
    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val API_USER = "api_user"
            const val API_FOLLOWER = "api_follower"
            const val API_FOLLOWING = "api_following"
        }
    }
}