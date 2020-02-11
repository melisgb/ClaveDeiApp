package com.example.myandroidapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myandroidapp.Model.Song

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

    //CRUD

    fun getSongs(): List<Song>
    {
        val listSongs = ArrayList<Song>()
        val selectQuery = "SELECT * FROM SONG"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst())
        {
            do{
                val song = Song()
                song.id = cursor.getInt(cursor.getColumnIndex("id"))
                song.title = cursor.getString(cursor.getColumnIndex("title"))
                song.artist = cursor.getString(cursor.getColumnIndex("artist"))
                song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
                song.tags = cursor.getString(cursor.getColumnIndex("tags"))

                listSongs.add(song)
            }
            while(cursor.moveToNext())
        }
        db.close()
        return listSongs
    }

    fun getSong(id: Long): Song?
    {
        val selectQuery = "SELECT * FROM SONG"
        val db = this.writableDatabase
        val cursor = db.query(true, "SONG", null, "id = ?", arrayOf(id.toString()), null, null, null, null)

        var song : Song? = null
        if(cursor.moveToFirst()) {
            song = Song()
            song.id = cursor.getInt(cursor.getColumnIndex("id"))
            song.title = cursor.getString(cursor.getColumnIndex("title"))
            song.artist = cursor.getString(cursor.getColumnIndex("artist"))
            song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
            song.tags = cursor.getString(cursor.getColumnIndex("tags"))
        }
//        db.close()
        cursor.close()

        return song
    }


    fun addSong(song:Song)
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", song.title)
        values.put("artist", song.artist)
        values.put("lyrics", song.lyrics)
        values.put("tags", song.tags)

        db.insert("SONG", null, values)
        db.close()
    }

    fun updateSong(song:Song):Int
    {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("id", song.id)
        values.put("title", song.title)
        values.put("artist", song.artist)
        values.put("lyrics", song.lyrics)
        values.put("tags", song.tags)

        return db.update("SONG", values, "id=?", arrayOf(song.id.toString()))
    }

    fun deleteSong(song:Song)
    {
        val db = this.writableDatabase

        db.delete("SONG","id=?", arrayOf(song.id.toString()))
        db.close()
    }




}


