package com.ahr.usergithub.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.AVATAR
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.BIO_DATA
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.FOLLOWER
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.FOLLOWER_API
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.FOLLOWING
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.FOLLOWING_API
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.NAME
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.PUBLIC_GISTS
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.PUBLIC_REPOSITORY
import com.ahr.usergithub.database.DatabaseContract.UserColumns.Companion.TABLE_NAME

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
                "$$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$AVATAR TEXT NOT NULL," +
                "$NAME TEXT NOT NULL," +
                "$BIO_DATA TEXT NOT NULL," +
                "$PUBLIC_REPOSITORY INTEGER NOT NULL," +
                "$PUBLIC_GISTS INTEGER NOT NULL," +
                "$FOLLOWER INTEGER NOT NULL," +
                "$FOLLOWING FOLLOWING NOT NULL," +
                "$FOLLOWER_API TEXT NOT NULL," +
                "$FOLLOWING_API TEXT NOT NULL," +
            ")"

        private const val SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}