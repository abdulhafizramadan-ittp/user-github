package com.ahr.usergithub.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_FOLLOWING
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.API_USER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.AVATAR
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.USERNAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DROP_TABLE_USER)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbfavoriteuser"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME " +
            "(" +
                "$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$USERNAME VARCHAR(100) NOT NULL," +
                "$AVATAR VARCHAR(250) NOT NULL," +
                "$API_USER VARCHAR(250) NOT NULL," +
                "$API_FOLLOWER VARCHAR(250) NOT NULL," +
                "$API_FOLLOWING VARCHAR(250) NOT NULL" +
            ")"

        private const val SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}