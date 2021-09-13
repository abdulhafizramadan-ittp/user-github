package com.ahr.usergithub.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ahr.usergithub.database.DatabaseContract.AUTHORITY
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.ahr.usergithub.database.UserHelper

class UserProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when(uriMather.match(uri)) {
            USER -> userHelper.queryAll()
            else -> null
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0

    }

    companion object {
        private const val USER = 1
        private val uriMather = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, USER)
        }
        private lateinit var userHelper: UserHelper
    }
}