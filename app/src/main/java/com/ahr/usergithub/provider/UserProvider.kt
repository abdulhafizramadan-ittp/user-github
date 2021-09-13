package com.ahr.usergithub.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ahr.usergithub.database.DatabaseContract.AUTHORITY
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.ahr.usergithub.database.UserHelper

class UserProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted = when(uriMather.match(uri)) {
            USER_USERNAME -> userHelper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when(uriMather.match(uri)) {
            USER -> userHelper.insert(values as ContentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$uri/$added")
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
        private const val USER_USERNAME = 2

        private val uriMather = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, USER)
            addURI(AUTHORITY, "$TABLE_NAME/*", USER_USERNAME)
        }
        private lateinit var userHelper: UserHelper
    }
}