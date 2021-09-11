package com.example.usergithub.database

import android.provider.BaseColumns

internal object DatabaseContract {
    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val AVATAR = "avatar"
            const val NAME = "name"
            const val BIO_DATA = "bio_data"
            const val PUBLIC_REPOSITORY = "public_repository"
            const val PUBLIC_GISTS = "public_gists"
            const val FOLLOWER = "follower"
            const val FOLLOWING = "following"
            const val FOLLOWER_API = "follower_api"
            const val FOLLOWING_API = "following_api"
        }
    }
}