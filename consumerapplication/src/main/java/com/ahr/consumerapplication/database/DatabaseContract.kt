package com.ahr.consumerapplication.database

import android.net.Uri
import android.provider.BaseColumns

internal object DatabaseContract {

    const val AUTHORITY = "com.ahr.usergithub"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            private const val TABLE_NAME = "user"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val API_USER = "api_user"
            const val API_FOLLOWER = "api_follower"
            const val API_FOLLOWING = "api_following"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}