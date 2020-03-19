package com.example.myandroidapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myandroidapp.Model.Song
import com.example.myandroidapp.Model.SongsList
import java.lang.IllegalStateException

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        var DATABASE_NAME = "songs_database"
        private val DATABASE_VERSION = 3

        private val CREATE_TABLE_SONG =
            ("CREATE TABLE SONG (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, artist TEXT, lyrics TEXT, tags TEXT);")

        private val CREATE_TABLE_LIST =
            ("CREATE TABLE LIST (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);")

        private val CREATE_TABLE_LIST_SONGS =
            ("CREATE TABLE LIST_SONGS (list_id INTEGER, song_id INTEGER, " +
                    "PRIMARY KEY (list_id, song_id), " +
                    "FOREIGN KEY (list_id) REFERENCES LIST(id), " +
                    "FOREIGN KEY (song_id) REFERENCES SONG(id)); ")

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
        Log.d("tables", "Tables created: SONG, LIST, LIST_SONGS");
    }

    //CRUD SONG

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

    fun getSongs(): List<Song> {
        val listSongs = ArrayList<Song>()
        val selectQuery = "SELECT * FROM SONG ORDER BY TITLE"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val song = Song(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    title = cursor.getString(cursor.getColumnIndex("title")),
                    artist = cursor.getString(cursor.getColumnIndex("artist")),
                    lyrics = cursor.getString(cursor.getColumnIndex("lyrics")),
                    tags = cursor.getString(cursor.getColumnIndex("tags"))
                )

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listSongs
    }

    fun getSong(id: Int): Song? {
        val db = this.writableDatabase
        val cursor =
            db.query(true, "SONG", null, "id = ?", arrayOf(id.toString()), null, null, null, null)

        var song: Song? = null
        if (cursor.moveToFirst()) {
            song = Song(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                title = cursor.getString(cursor.getColumnIndex("title")),
                artist = cursor.getString(cursor.getColumnIndex("artist")),
                lyrics = cursor.getString(cursor.getColumnIndex("lyrics")),
                tags = cursor.getString(cursor.getColumnIndex("tags"))
            )
        }
        cursor.close()
//        db.close()

        return song
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

        val listSongs = ArrayList<Song>()
        val selectQuerySongs = "SELECT ls.list_id FROM LIST_SONGS ls WHERE ls.song_id = ${song.id}"
        val cursorSongs = db.rawQuery(selectQuerySongs, null)
        val listID : Int
        if (cursorSongs.moveToFirst()) {
            throw IllegalStateException("Esta cancion esta asociada a una lista. No se puede eliminar")
        }
        else{
            //        After checking that the song doesnot belong to any list
            db.delete("SONG", "id=?", arrayOf(song.id.toString()))
        }
        cursorSongs.close()
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
                val song = Song(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    title = cursor.getString(cursor.getColumnIndex("title")),
                    artist = cursor.getString(cursor.getColumnIndex("artist")),
                    lyrics = cursor.getString(cursor.getColumnIndex("lyrics")),
                    tags = cursor.getString(cursor.getColumnIndex("tags"))
                )

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
                val song = Song(
                id = cursor.getInt(cursor.getColumnIndex("id")),
                title = cursor.getString(cursor.getColumnIndex("title")),
                artist = cursor.getString(cursor.getColumnIndex("artist")),
                lyrics = cursor.getString(cursor.getColumnIndex("lyrics")),
                tags = cursor.getString(cursor.getColumnIndex("tags"))
                )

                listSongs.add(song)
            } while (cursor.moveToNext())
        }
        db.close()
        return listSongs
    }



    //CRUD USER LISTS
    fun addSongsList(name : String, songs : List<Song>) : SongsList {
        val db = this.writableDatabase
        val listName =  name

        val valuesList = ContentValues()
        valuesList.put("name", listName)
        val rowID = db.insert("LIST",null, valuesList)

        val cursor = db.query(true, "LIST", arrayOf("id"), "ROWID = ?", arrayOf(rowID.toString()), null, null, null, null)
        val listID : Int
        if (cursor.moveToFirst()) {
            listID = cursor.getInt(cursor.getColumnIndex("id"))
        }
        else{
            throw IllegalStateException("Lista no existe")
        }
        cursor.close()

        val songsInserted = addSongsToSpecificList(listID, songs)
        return SongsList(listID, listName, songsInserted)

//        db.close()
    }

    private fun addSongsToSpecificList(listID: Int, songs : List<Song>) : HashMap<Int, Song> {
        val db = this.writableDatabase
        val cursor = db.query(false, "LIST", arrayOf("id"), "id = ?", arrayOf(listID.toString()), null, null, null, null)

        val listExists = cursor.moveToFirst()
        cursor.close()
        val songsPerList = HashMap<Int, Song>()
        if(listExists){
            val valuesSongsList = ContentValues()
            for (song in songs) {
                valuesSongsList.put("list_id", listID)
                valuesSongsList.put("song_id", song.id)

                db.insert("LIST_SONGS",null, valuesSongsList)

            }

            for (song in songs) {
                songsPerList[song.id] = song
            }
            return songsPerList
        }
        else return songsPerList
    }

    fun getSummaryLists() : ArrayList<SongsList> {
        val selectQuery = "SELECT * FROM LIST ORDER BY NAME"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        val allLists = ArrayList<SongsList>()
        if (cursor.moveToFirst()) {
            do {
                val list = SongsList(
                    id = cursor.getInt(cursor.getColumnIndex("id")),
                    name = cursor.getString(cursor.getColumnIndex("name")),
                    songs = HashMap<Int, Song>()
                )
                allLists.add(list)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return allLists
    }

    fun getSongsList(listID: Int) : SongsList {
        val db = this.writableDatabase
        val selectQueryList = "SELECT l.* FROM LIST l WHERE l.id = $listID ORDER BY l.NAME"
        val cursorList = db.rawQuery(selectQueryList, null)

        val listID = listID
        cursorList.moveToFirst()
        val listName = cursorList.getString(cursorList.getColumnIndex("name"))
        cursorList.close()

        val listSongs = ArrayList<Song>()
        val selectQuerySongs = "SELECT ls.list_id, s.* FROM LIST_SONGS ls LEFT JOIN SONG s ON ls.song_id = s.id WHERE ls.list_id = $listID ORDER BY s.TITLE"
        val cursorSongs = db.rawQuery(selectQuerySongs, null)

        if (cursorSongs.moveToFirst()) {
            do {
                val song = Song(
                    id = cursorSongs.getInt(cursorSongs.getColumnIndex("id")),
                    title = cursorSongs.getString(cursorSongs.getColumnIndex("title")),
                    artist = cursorSongs.getString(cursorSongs.getColumnIndex("artist")),
                    lyrics = cursorSongs.getString(cursorSongs.getColumnIndex("lyrics")),
                    tags = cursorSongs.getString(cursorSongs.getColumnIndex("tags"))
                )

                listSongs.add(song)
            } while (cursorSongs.moveToNext())
        }
        cursorSongs.close()
        val songsPerList = HashMap<Int, Song>()

        for (song in listSongs) {
            songsPerList[song.id] = song
        }
        return SongsList(listID, listName, songsPerList)
    }

    fun updateSongsList(songsList: SongsList): SongsList {
        val db = this.writableDatabase

//        Deleting all songs related to list, to later add as new
        db.delete("LIST_SONGS", "list_id = ?", arrayOf(songsList.id.toString()))

        val valuesList = ContentValues()
        valuesList.put("name", songsList.name)
        val qtyRowsAffec = db.update("LIST", valuesList, "id=?", arrayOf(songsList.id.toString()))

        val songsInserted = addSongsToSpecificList(songsList.id, songsList.songs.values.toList())

        return SongsList(songsList.id, songsList.name, songsInserted)
    }


    fun deleteList(songsList: SongsList) {
        val db = this.writableDatabase

        db.delete("LIST_SONGS", "list_id = ?", arrayOf(songsList.id.toString()))
        db.delete("LIST", "id=?", arrayOf(songsList.id.toString()))

        db.close()
    }

    fun deleteSongsList(songID : Int, songsList : SongsList) {
        val db = this.writableDatabase
        val listID = songsList.id
        db.delete("LIST_SONGS", "song_id=? and list_id =?", arrayOf(songID.toString(), listID.toString()))
        db.close()
    }

    fun searchSongsListByName(listName: String) : Int {
        val db = this.writableDatabase
        val selectQueryList = "SELECT l.* FROM LIST l WHERE l.name = '$listName'"
        val cursorList = db.rawQuery(selectQueryList, null)
        var listID = 0

        if (cursorList.moveToFirst()) {
            do {
                listID = cursorList.getInt(cursorList.getColumnIndex("id"))
            } while (cursorList.moveToNext())
        }
        cursorList.close()

        return listID
    }

}


