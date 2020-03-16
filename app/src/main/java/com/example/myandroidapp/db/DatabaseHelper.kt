package com.example.myandroidapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        var DATABASE_NAME = "songs_database"
        private val DATABASE_VERSION = 1

        private val CREATE_TABLE_SONG =
            ("CREATE TABLE SONG (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, artist TEXT, lyrics TEXT, tags TEXT);")

        private val CREATE_TABLE_LIST =
            ("CREATE TABLE LIST (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);")

        private val CREATE_TABLE_LIST_SONGS =
            ("CREATE TABLE LIST_SONGS (list_id INTEGER, song_id INTEGER, PRIMARY KEY (list_id, song_id), " +
                    "FOREIGN KEY (list_id) REFERENCES LIST(id));")

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_SONG)
        db.execSQL(CREATE_TABLE_LIST)
        db.execSQL(CREATE_TABLE_LIST_SONGS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS SONG;")
        db.execSQL("DROP TABLE IF EXISTS LIST;")
        db.execSQL("DROP TABLE IF EXISTS LIST_SONGS;")
        onCreate(db)
    }

    //CRUD

    fun getSongs(): List<Song> {
        val listSongs = ArrayList<Song>()
        val selectQuery = "SELECT * FROM SONG ORDER BY TITLE"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val song = Song()
                song.id = cursor.getInt(cursor.getColumnIndex("id"))
                song.title = cursor.getString(cursor.getColumnIndex("title"))
                song.artist = cursor.getString(cursor.getColumnIndex("artist"))
                song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
                song.tags = cursor.getString(cursor.getColumnIndex("tags"))

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        db.close()
        return listSongs
    }

    fun getSong(id: Long): Song? {
        val db = this.writableDatabase
        val cursor =
            db.query(true, "SONG", null, "id = ?", arrayOf(id.toString()), null, null, null, null)

        var song: Song? = null
        if (cursor.moveToFirst()) {
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

    fun getSongsList(keyword: String) : List<Song> {
        val listSongs = ArrayList<Song>()
        val selectQuery = "SELECT s.* FROM LIST l LEFT JOIN SONG s ON l.song_id = s.id WHERE l.name = $keyword ORDER BY s.TITLE"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val song = Song()
                song.id = cursor.getInt(cursor.getColumnIndex("id"))
                song.title = cursor.getString(cursor.getColumnIndex("title"))
                song.artist = cursor.getString(cursor.getColumnIndex("artist"))
                song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
                song.tags = cursor.getString(cursor.getColumnIndex("tags"))

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        db.close()
        return listSongs
    }

//    fun getAllLists(): ArrayList<SongsList> {
//        val listSongsList = ArrayList<SongsList>()
//        val selectQuery = "SELECT l.id, l.name, s.* FROM LIST l LEFT JOIN SONG s ON l.song_id = s.id ORDER BY l.NAME, s.TITLE"
//        val db = this.writableDatabase
//        val cursor = db.rawQuery(selectQuery, null)
//        if (cursor.moveToFirst()) {
//            do {
//                val songsList = SongsList()
//                songsList.id = cursor.getInt(cursor.getColumnIndex("id"))
//                songsList.name = cursor.getString(cursor.getColumnIndex("title"))
//
//
//                listSongsList.contains()
//
//                if(!listSongsList.contains()
//                    song.tags = cursor.getString(cursor.getColumnIndex("tags"))
//
//                listSongsList.add(song)
//            } while (cursor.moveToNext())
//        }
//        db.close()
//        return listSongsList
//    }


//    fun addToFavorites(songs : List<Song>): Boolean {
////        if new
//        if(db.query())
//        for(song in songs){
//
//        }
//
////        if does not exist
////        if exists
//
//    }

    fun addSong(song: Song) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("title", song.title)
        values.put("artist", song.artist)
        values.put("lyrics", song.lyrics)
        values.put("tags", song.tags)

        db.insert("SONG", null, values)
        db.close()
    }

    fun updateSong(song: Song): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("id", song.id)
        values.put("title", song.title)
        values.put("artist", song.artist)
        values.put("lyrics", song.lyrics)
        values.put("tags", song.tags)

        return db.update("SONG", values, "id=?", arrayOf(song.id.toString()))
    }

    fun deleteSong(song: Song) {
        val db = this.writableDatabase

        db.delete("SONG", "id=?", arrayOf(song.id.toString()))
        db.close()
    }

    fun searchSongs(keyword: String): List<Song> {
        val listSongs = ArrayList<Song>()
        val db = this.writableDatabase

        val cursor = db.query(
            true,
            "SONG",
            null,
            "title LIKE ? or artist LIKE ? or lyrics LIKE ? or tags LIKE ?",
            arrayOf("%$keyword%", "%$keyword%", "%$keyword%", "%$keyword%"),
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val song = Song()
                song.id = cursor.getInt(cursor.getColumnIndex("id"))
                song.title = cursor.getString(cursor.getColumnIndex("title"))
                song.artist = cursor.getString(cursor.getColumnIndex("artist"))
                song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
                song.tags = cursor.getString(cursor.getColumnIndex("tags"))

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        db.close()
        return listSongs
    }

    fun searchSongsByTags(keyword: String): List<Song> {
        val listSongs = ArrayList<Song>()
        val db = this.writableDatabase

        val cursor = db.query(
            true,
            "SONG",
            null,
            "title LIKE ? or tags LIKE ?",
            arrayOf("%$keyword%", "%$keyword%"),
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val song = Song()
                song.id = cursor.getInt(cursor.getColumnIndex("id"))
                song.title = cursor.getString(cursor.getColumnIndex("title"))
                song.artist = cursor.getString(cursor.getColumnIndex("artist"))
                song.lyrics = cursor.getString(cursor.getColumnIndex("lyrics"))
                song.tags = cursor.getString(cursor.getColumnIndex("tags"))

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        db.close()
        return listSongs
    }

}


