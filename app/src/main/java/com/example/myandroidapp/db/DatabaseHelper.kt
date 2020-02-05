package com.example.myandroidapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){



    companion object {
        var DATABASE_NAME = "songs_database"
        private val DATABASE_VERSION = 1

        private val CREATE_TABLE_SONG = ("CREATE TABLE SONG (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, artist TEXT, lyrics TEXT, tags TEXT);")

        private val CREATE_TABLE_LIST = ("CREATE TABLE LIST (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, song_id INTEGER);")

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SONG)
        db.execSQL(CREATE_TABLE_LIST)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS SONG;")
        db.execSQL("DROP TABLE IF EXISTS LIST;")
        onCreate(db)
    }

}


