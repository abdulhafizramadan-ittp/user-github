package com.ahr.usergithub.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.provider.BaseColumns._ID
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.USERNAME

class UserHelper(context: Context) {
    private var databaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    @Throws(SQLiteException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen) {
            database.close()
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID DESC"
        )
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun updateByUsername(username: String, values: ContentValues): Int {
        return database.update(DATABASE_TABLE, values, "$$USERNAME = ?", arrayOf(username))
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME = ?", arrayOf(username))
    }

    companion object {
        private const val DATABASE_TABLE = DatabaseContract.UserColumns.TABLE_NAME

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }
}