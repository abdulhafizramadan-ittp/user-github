package com.ahr.consumerapplication.helper

import android.database.Cursor
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.API_FOLLOWER
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.API_FOLLOWING
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.API_USER
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.AVATAR
import com.ahr.consumerapplication.database.DatabaseContract.UserColumns.Companion.USERNAME
import com.ahr.consumerapplication.model.User

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val apiUser = getString(getColumnIndexOrThrow(API_USER))
                val apiFollower = getString(getColumnIndexOrThrow(API_FOLLOWER))
                val apiFollowing = getString(getColumnIndexOrThrow(API_FOLLOWING))
                userList.add(User(username, avatar, apiUser, apiFollower, apiFollowing))
            }
        }

        return userList
    }
}